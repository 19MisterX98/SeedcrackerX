#include <thread>
#include <vector>
#include <iostream>
#include <string>
#include <fstream>
#include <cstdlib>
#include <utility>
#include <mutex>
#include <map>
#include <algorithm>
#define _USE_MATH_DEFINES
#include <cmath>
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

#define SKIP 8
#define THREAD_SIZE 256LLU
#define BLOCK_SIZE (1LLU<<29)
#define BATCH_SIZE (THREAD_SIZE * BLOCK_SIZE * SKIP)


__managed__ uint32_t count = 0;
__managed__ uint64_t seedBuff[60000];

#define SEED_SPACE (1LLU<<48)

__global__ __launch_bounds__(THREAD_SIZE) void InitalFilter(const uint64_t offset) {
	uint64_t seed = (uint64_t)blockIdx.x * (uint64_t)blockDim.x * SKIP + (uint64_t)threadIdx.x  * SKIP + offset;

    if (((seed * 25214903917LLU)&((1LLU<<48)-1LLU)) < 225179967946752LLU) return;

THIS_STATEMENT_WILL_BE_REPLACED

}

int main() {
	SETGPU(0);
	std::ofstream outfile("output_seeds.dat", std::ofstream::binary);
	std::ofstream updates("updates.dat", std::ofstream::binary);
	for (uint64_t offset = 0; offset < SEED_SPACE; offset += BATCH_SIZE) {
		uint64_t start = millis();
		
		count = 0;
		InitalFilter<<<BLOCK_SIZE, THREAD_SIZE>>>(offset);
		GPU_ASSERT(cudaPeekAtLastError());	
		GPU_ASSERT(cudaDeviceSynchronize());
		GPU_ASSERT(cudaPeekAtLastError());

		if (count != 0) {
            for (uint64_t index = 0; index < count; index++) {
                outfile << seedBuff[index] << std::endl;
                outfile.flush();
            }
		}
        uint64_t step1 = millis()-start;
        updates << "Finished gpu: time_millis:" << step1 << ", output: " << count << ", run:" << ((SEED_SPACE - offset)/BATCH_SIZE)  << std::endl;
        updates.flush();
		std::cout << "Finished gpu: time_millis:" << step1 << ", output: " << count << ", run:" << ((SEED_SPACE - offset)/BATCH_SIZE)  << std::endl;
	}
	outfile.close();
    updates.close();
	return 1;
}