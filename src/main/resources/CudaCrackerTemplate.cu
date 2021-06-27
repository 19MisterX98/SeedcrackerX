#include <thread>
#include <vector>
#include <iostream>
#include <string>
#include <fstream>
#include <stdlib.h>
#include <utility>
#include <mutex>
#include <map>
#include <algorithm>
#define _USE_MATH_DEFINES
#include <math.h>
#include <chrono>


uint64_t millis() {return (std::chrono::duration_cast< std::chrono::milliseconds >(std::chrono::system_clock::now().time_since_epoch())).count();}

#define GPU_ASSERT(code) gpuAssert((code), __FILE__, __LINE__)
inline void gpuAssert(cudaError_t code, const char *file, int line) {
  if (code != cudaSuccess) {
	fprintf(stderr, "GPUassert: %s (code %d) %s %d\n", cudaGetErrorString(code), code, file, line);
	exit(code);
  }
}

#define SETGPU(gpuId) cudaSetDevice(gpuId);\
	GPU_ASSERT(cudaPeekAtLastError());\
	GPU_ASSERT(cudaDeviceSynchronize());\
	GPU_ASSERT(cudaPeekAtLastError());

#define DEVICEABLE __host__ __device__



#define THREAD_SIZE 256LLU
#define BLOCK_SIZE (1LLU<<27) //(1LLU<<29)
#define BATCH_SIZE (THREAD_SIZE * BLOCK_SIZE)




__managed__ uint32_t count = 0;
__managed__ uint64_t seedBuff[60000000];


__managed__ uint32_t countOut = 0;
__managed__ uint64_t outputSeedBuff[6000];//Max seed output for the secondary filter

#define TREE_X INIT_TREE_INNER_X
#define TREE_Z INIT_TREE_INNER_Z



#define signed_seed_t int64_t

#define MODULUS (1LL << 48)
#define SQUARE_SIDE (MODULUS / 16)
#define X_TRANSLATE 0
#define Z_TRANSLATE 11
#define L00 7847617LL
#define L01 (-18218081LL)
#define L10 4824621LL
#define L11 24667315LL
#define LI00 (24667315.0 / 16)
#define LI01 (18218081.0 / 16)
#define LI10 (-4824621.0 / 16)
#define LI11 (7847617.0 / 16)

#define CONST_MIN(a, b) ((a) < (b) ? (a) : (b))
#define CONST_MIN4(a, b, c, d) CONST_MIN(CONST_MIN(a, b), CONST_MIN(c, d))
#define CONST_MAX(a, b) ((a) > (b) ? (a) : (b))
#define CONST_MAX4(a, b, c, d) CONST_MAX(CONST_MAX(a, b), CONST_MAX(c, d))
#define CONST_FLOOR(x) ((x) < (signed_seed_t) (x) ? (signed_seed_t) (x) - 1 : (signed_seed_t) (x))
#define CONST_CEIL(x) ((x) == (signed_seed_t) (x) ? (signed_seed_t) (x) : CONST_FLOOR((x) + 1))
#define CONST_LOWER(x, m, c) ((m) < 0 ? ((x) + 1 - (double) (c) / MODULUS) * (m) : ((x) - (double) (c) / MODULUS) * (m))
#define CONST_UPPER(x, m, c) ((m) < 0 ? ((x) - (double) (c) / MODULUS) * (m) : ((x) + 1 - (double) (c) / MODULUS) * (m))

// for a parallelogram ABCD https://media.discordapp.net/attachments/668607204009574411/671018577561649163/unknown.png
#define B_X LI00
#define B_Z LI10
#define C_X (LI00 + LI01)
#define C_Z (LI10 + LI11)
#define D_X LI01
#define D_Z LI11
#define LOWER_X CONST_MIN4(0, B_X, C_X, D_X)
#define LOWER_Z CONST_MIN4(0, B_Z, C_Z, D_Z)
#define UPPER_X CONST_MAX4(0, B_X, C_X, D_X)
#define UPPER_Z CONST_MAX4(0, B_Z, C_Z, D_Z)
#define ORIG_SIZE_X (UPPER_X - LOWER_X + 1)
#define SIZE_X CONST_CEIL(ORIG_SIZE_X - D_X)
#define SIZE_Z CONST_CEIL(UPPER_Z - LOWER_Z + 1)
#define TOTAL_WORK_SIZE (SIZE_X * SIZE_Z)

#define SEED_SPACE TOTAL_WORK_SIZE


__global__ __launch_bounds__(THREAD_SIZE) void InitalFilter(const uint64_t offset) {
	uint64_t idx = (((uint64_t)blockIdx.x * (uint64_t)blockDim.x + (uint64_t)threadIdx.x))+offset;

    signed_seed_t lattice_x = (int64_t)(idx%SIZE_X) + LOWER_X;
    signed_seed_t lattice_z = (int64_t)(idx/SIZE_X) + LOWER_Z;

    lattice_z += (B_X * lattice_z < B_Z * lattice_x) * SIZE_Z;
    if (D_X * lattice_z > D_Z * lattice_x) {
        lattice_x += B_X;
        lattice_z += B_Z;
    }

    lattice_x += (signed_seed_t) (TREE_X * LI00 + TREE_Z * LI01);
    lattice_z += (signed_seed_t) (TREE_X * LI10 + TREE_Z * LI11);

    uint64_t seed = (lattice_x * L00 + lattice_z * L01 + X_TRANSLATE)  & (MODULUS-1);

	PRIMARY_TREE_FILTER

	//TODO: Have different seed buffers per thread or somthing, so that the atomicAdd isnt a bottleneck
	seedBuff[atomicAdd(&count, 1)] = seed;
}





AUX_TREE_FUNCTIONS_REPLACEMENT



#define NEXT_INT_16(seed) (((seed = ((seed * 0x5DEECE66DLLU + 0xBLLU)&((1LLU<<48)-1)))>>(48-4)))

#define TREE_TEST(testMethod, index, expected_x, expected_z, IF_TYPE) IF_TYPE ((!(mask & (1<<index))) && x_pos == expected_x && z_pos == expected_z) mask |= ((uint8_t)testMethod(seed))<<index;
#define TARGET_MASK ((1<<AUXILIARY_TREE_COUNT)-1)
__global__ __launch_bounds__(THREAD_SIZE) void SecondaryFilter() {
	uint64_t idx = ((((uint64_t)blockIdx.x * (uint64_t)blockDim.x + (uint64_t)threadIdx.x)));
	if (idx >= count)
		return;
	uint64_t seed = seedBuff[idx];
	seed = LCG_REVERSE_STAGE_2_REPLACEMENT;
	
	uint8_t mask = 0;
	int32_t x_pos;
	int32_t z_pos = NEXT_INT_16(seed);
	for (int32_t index = 0; index < MAX_TREE_RNG_RANGE_REPLACEMENT * 2 && mask != TARGET_MASK; index++) {
        x_pos = z_pos;
		z_pos = NEXT_INT_16(seed);
		
        AUX_TREE_TEST_INNER_LOOP_CALL_REPLACEMENT
		

	}
	
	
	if (mask != TARGET_MASK)
		return;
	
	outputSeedBuff[atomicAdd(&countOut, 1)] = seedBuff[idx];
}











int main() {
	SETGPU(0);
	std::ofstream outfile("output_seeds.dat", std::ofstream::binary);
	for (uint64_t offset = 0; offset < SEED_SPACE; offset += BATCH_SIZE) {
		uint64_t start = millis();
		
		count = 0;
		countOut = 0;
		InitalFilter<<<BLOCK_SIZE, THREAD_SIZE>>>(offset);
		GPU_ASSERT(cudaPeekAtLastError());	
		GPU_ASSERT(cudaDeviceSynchronize());
		GPU_ASSERT(cudaPeekAtLastError());
		uint64_t step1 = millis()-start;
		start = millis();

        uint64_t step2 = 0;
        uint64_t step3 = 0;
		if (count != 0) {
            SecondaryFilter<<<ceil((double)count/THREAD_SIZE), THREAD_SIZE>>>();
            GPU_ASSERT(cudaPeekAtLastError());
            GPU_ASSERT(cudaDeviceSynchronize());
            GPU_ASSERT(cudaPeekAtLastError());
            step2 = millis()-start;
            start = millis();

            for (uint64_t index = 0; index < countOut; index++) {
                outfile << outputSeedBuff[index] << std::endl;
                outfile.flush();
            }
            step3 = millis()-start;
		}

		std::cout << "Finished gpu: " << (step1+step2+step3) << ", " << step1 << ", " << step2 << ", " << step3 << ", " << count << ", " << countOut << ", " << ((SEED_SPACE - offset)/BATCH_SIZE)  << std::endl;
	}
	outfile.close();
	return 1;
}