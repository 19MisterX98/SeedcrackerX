package kaptainwutax.seedcrackerX.finder.surface;

import com.seedfinding.mccore.util.math.NextLongReverser;
import com.seedfinding.mcseed.lcg.LCG;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.cracker.storage.TimeMachine;
import kaptainwutax.seedcrackerX.finder.BlockFinder;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.render.Color;
import kaptainwutax.seedcrackerX.render.Cube;
import kaptainwutax.seedcrackerX.util.Log;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public class NetherRoofFinder extends BlockFinder {

    protected static List<BlockPos> SEARCH_POSITIONS = CHUNK_POSITIONS.stream().filter(block -> block.getY() == 123).toList();

    public static final long mask = (1L << 48) - 1;

    public static Process crackerProcess = null;


    public NetherRoofFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.BEDROCK);
        this.searchPositions = SEARCH_POSITIONS;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isNether(dimension);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new NetherRoofFinder(world, chunkPos));
        return finders;
    }

    @Override
    public List<BlockPos> findInChunk() {
        List<BlockPos> result = super.findInChunk();

        if (SeedCracker.get().getDataStorage().addNetherRoofData(result, this::onDataAdded)) {
            for (BlockPos pos : result) {
                this.renderers.add(new Cube(pos, new Color(2, 2, 2)));
            }
        }
        return result;
    }

    private long hashCode(Vec3i vec3i) {
        return MathHelper.hashCode(vec3i) & (1L << 48) - 1;
    }

    private void onDataAdded(DataStorage dataStorage) {
        if (dataStorage.netherRoofData.size() > 20) {
            if (crackerProcess != null) {
                crackerProcess.destroy();
            }

            StringBuilder builder = new StringBuilder();
            List<BlockPos> data = new ArrayList<>(dataStorage.netherRoofData);
            List<Predicate<Long>> cpuFilter = new ArrayList<>();

            for (BlockPos pos : dataStorage.netherRoofData) {
                cpuFilter.add(seed -> ((((seed ^ hashCode(pos)) * 25214903917L)&mask) < 225179967946752L));
            }

            long firstHash = hashCode(data.remove(0));

            //we dont want too much data here since we use a probability based search
            for (int i = 0; i < 20; i++) {
                long combinedHash = hashCode(data.get(i)) ^ firstHash;
                generateCheck(combinedHash, builder);
            }
            builder.append("seedBuff[atomicAdd(&count, 1)] = seed ^ ")
                    .append(firstHash)
                    .append("LLU;");

            URL crackerFile = Thread.currentThread().getContextClassLoader().getResource("BedrockCracker.cu");
            String source = null;
            try {
                source = IOUtils.toString(crackerFile, Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }

            source = source.replaceFirst("THIS_STATEMENT_WILL_BE_REPLACED", builder.toString());
            Path outFile = FabricLoader.getInstance().getConfigDir().resolve("BedrockCracker.cu");
            try {
                Files.write(outFile, source.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }


            List<String> commandList = new ArrayList<>();
            commandList.add("nvcc");
            commandList.add("-o");
            commandList.add("BedrockCracker");
            commandList.add("BedrockCracker.cu");
            run(commandList, false);

            commandList.clear();

            commandList.add("./BedrockCracker");
            run(commandList, true);

            List<Long> outputSeeds = new ArrayList<>();
            File seedsFile = FabricLoader.getInstance().getConfigDir().resolve("output_seeds.dat").toFile();
            try {
                Scanner scanner = new Scanner(seedsFile);
                while (scanner.hasNextLong()) {
                    outputSeeds.add(scanner.nextLong());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Set<Long> potentialSeeds = new HashSet<>();

            outputSeeds.forEach(seed -> {
                lowerBits:
                for (int lowerBits = -(1<<8); lowerBits < (1<<8); lowerBits++) {
                    for (Predicate<Long> filter : cpuFilter) {
                        if (filter.test(seed+lowerBits)) {
                            continue lowerBits;
                        }
                    }
                    potentialSeeds.add(seed+lowerBits);
                }
            });
            for (long seed : potentialSeeds) {
                seed = seed ^ LCG.JAVA.multiplier;
                reverseToStructureSeeds(seed).forEach(dataStorage.getTimeMachine().structureSeeds::add);
            }
            dataStorage.getTimeMachine().poke(TimeMachine.Phase.BIOMES);

        }
    }

    private static void generateCheck(long xor, StringBuilder builder) {
        builder.append("if ((((seed ^ ")
                .append(xor)
                .append("LLU) * ")
                .append(LCG.JAVA.multiplier)
                .append("LLU)&((1LLU<<48)-1LLU)) < 215504279044096LLU) return;\n");
    }

    private void run(List<String> commandList, boolean addToStatic) {
        ProcessBuilder pBuilder = new ProcessBuilder(commandList);
        pBuilder.directory(FabricLoader.getInstance().getConfigDir().toFile());
        pBuilder.redirectErrorStream(true);

        Process process;

        try {
            process = pBuilder.start();
            if (addToStatic) {
                crackerProcess = process;
            }
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                Log.debug(s);
            }
            Log.debug("exit value: "+process.waitFor());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<Long> reverseToStructureSeeds(long seed) {
        List<Long> outputs = new ArrayList<>();
        seed = seed & mask;

        long hashcode = "minecraft:bedrock_roof".hashCode() & mask;

        List<Long> outSeeds = NextLongReverser.getSeeds(seed);
        for (long revSeed : outSeeds) {
            revSeed = revSeed ^ LCG.JAVA.multiplier ^ hashcode;

            List<Long> outSeeds2 = NextLongReverser.getSeeds(revSeed);
            for (long endRes : outSeeds2) {
                endRes = endRes ^ LCG.JAVA.multiplier;
                outputs.add(endRes);
            }
        }
        return outputs;
    }
}