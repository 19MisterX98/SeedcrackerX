package kaptainwutax.seedcrackerX.cracker.decorator;

import kaptainwutax.biomeutils.biome.Biome;
import kaptainwutax.biomeutils.biome.Biomes;
import kaptainwutax.mcutils.rand.ChunkRand;
import kaptainwutax.mcutils.state.Dimension;
import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.mcutils.version.VersionMap;
import kaptainwutax.seedcrackerX.cracker.decorator.decoratorData.TreeData;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.cracker.storage.TimeMachine;
import kaptainwutax.seedcrackerX.util.Log;
import kaptainwutax.seedutils.lcg.LCG;
import me.cortex.TreeCracker.program.TreeCrackerProgram;
import me.cortex.TreeCracker.trees.ICrackableTree;
import me.cortex.TreeCracker.trees.Simple116BlobTree;
import me.cortex.TreeCracker.trees.TreePos;
import mjtb49.hashreversals.PopulationReverser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Tree extends Decorator<Decorator.Config, Tree.Data> {

    private static boolean alreadyRun = false;

    public static final VersionMap<Decorator.Config> CONFIGS = new VersionMap<Decorator.Config>()
            .add(MCVersion.v1_17, new EmeraldOre.Config(8, 2));

    public Tree(MCVersion version) {
        super(CONFIGS.getAsOf(version), version);
    }

    public Tree(Decorator.Config config) {
        super(config, null);
    }

    @Override
    public String getName() {
        return "tree";
    }

    @Override
    public boolean canStart(Tree.Data data, long structureSeed, ChunkRand rand) {
        return true;
    }

    @Override
    public boolean isValidDimension(Dimension dimension) {
        return dimension == Dimension.OVERWORLD;
    }

    @Override
    public boolean isValidBiome(Biome biome) {
        return biome.getCategory().equals(Biome.Category.FOREST) && !biome.equals(Biomes.DARK_FOREST);
    }

    @Override
    public Dimension getValidDimension() {
        return Dimension.OVERWORLD;
    }


    public Tree.Data at(int chunkX, int chunkZ, Biome biome, List<TreeData> treeDataList) {
        return new Tree.Data(this, chunkX, chunkZ, biome, treeDataList);
    }

    public static class Data extends Decorator.Data<Tree> {
        public final List<TreeData> treeDataList;
        public final List<TreeData> incompleteTrees;

        public Data(Tree feature, int chunkX, int chunkZ, Biome biome, List<TreeData> treeDataList) {
            super(feature, chunkX, chunkZ, biome);
            this.treeDataList = treeDataList.stream().filter(treeData -> treeData.complete).collect(Collectors.toList());
            this.incompleteTrees = treeDataList.stream()/*.filter(treeData -> !treeData.complete)*/.collect(Collectors.toList());
        }

        public void onDataAdded(DataStorage dataStorage) {
            System.out.println("======================================");
            System.out.println("ChunkX: " + this.chunkX + " ChunkZ: " + this.chunkZ);
            treeDataList.forEach(tree -> System.out.println(tree.toString()));
            if (!alreadyRun) {
                List<Simple116BlobTree> trees = new ArrayList<>();
                treeDataList.forEach(treeData -> {
                    trees.add(new Simple116BlobTree(treeData.type, new TreePos(treeData.pos.getX(), treeData.pos.getZ()), treeData.height, treeData.leavesToArray()));
                });

                TreeCrackerProgram program = new TreeCrackerProgram(trees.toArray(new ICrackableTree[trees.size()]));

                System.out.println("runnig \"compiler\" for ChunkX: " + this.chunkX + " ChunkZ: " + this.chunkZ);
                File outputFolder = new File(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toString()+"/cracker");
                File cuFile = new File(outputFolder,"out.cu");
                File outputFile = new File(outputFolder,"output_seeds.dat");
                outputFolder.mkdir();
                try {
                    Runtime runtime = Runtime.getRuntime();
                    program.generateCracker().exportSource(cuFile);
                    Process process = runtime.exec("cmd /c nvcc -o out out.cu", null, outputFolder.getAbsoluteFile());
                    process.waitFor();
                    StringBuilder output = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                    System.out.println(output);
                    Thread.sleep(2000);
                    process = runtime.exec("cmd /c out", null, outputFolder.getAbsoluteFile());
                    System.out.println("finished waiting for process");
                    reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        Log.warn(line);
                        System.out.println(line);
                        if(MinecraftClient.getInstance().world == null) {
                            process.destroyForcibly();
                            process.waitFor();
                            System.out.println("killed");
                        }
                    }

                    output = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                    System.out.println(output);
                    process.waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

                String treeSeed;
                Set<Long> structureSeeds = new HashSet<>();
                try {
                    Scanner scanner = new Scanner(outputFile);
                    while (scanner.hasNextLine()) {
                        treeSeed = scanner.nextLine();
                        Log.warn("cracked seed: " + treeSeed);
                        structureSeeds.addAll(reverse(Long.parseLong(treeSeed)));
                    }
                } catch (FileNotFoundException ignored) {
                }

                Log.debug("====================================");
                structureSeeds.forEach(seed -> Log.printSeed("Found structure seed ${SEED}.", seed));
                System.out.println();
                dataStorage.getTimeMachine().structureSeeds = new ArrayList<>();
                dataStorage.getTimeMachine().structureSeeds.addAll(structureSeeds);
                dataStorage.getTimeMachine().poke(TimeMachine.Phase.BIOMES);
                alreadyRun = true;
            }
        }

        private Set<Long> reverse(long treeSeed) {
            Set<Long> seeds = new HashSet<>();
            ChunkRand backwardRand = new ChunkRand(treeSeed,false);
            ChunkRand simRand = new ChunkRand();
            backwardRand.advance(-200);
            for (int i = 200; i > 0; i--) {
                for (TreeData tree : incompleteTrees) {
                    simRand.setSeed(backwardRand.getSeed(), false);
                    if (testTree(tree, simRand)) {
                        System.out.println("index before tree: "+ i);
                        generatestructureSeeds(backwardRand.getSeed(), seeds);
                        return seeds;
                    }
                }
                backwardRand.advance(1);
            }
            return seeds;
        }

        private boolean testTree(TreeData tree, ChunkRand simRand) {
            if (tree.pos.getX() != simRand.nextInt(16) || tree.pos.getZ() != simRand.nextInt(16)) return false;
            if (tree.type.equals(Simple116BlobTree.FOREST_OAK_TREE)) {
                if (simRand.nextFloat() < 0.2F || simRand.nextFloat() < 0.1F || tree.height != simRand.nextInt(3) + 4) return false;
            } else if (tree.type.equals(Simple116BlobTree.FOREST_BIRCH_TREE)) {
                if (simRand.nextFloat() > 0.2F || tree.height != simRand.nextInt(3) + 5) return false;
            } else return false;
            System.out.println(tree.leaves);
            //skip 2nd random height and 4 always false leaves
            simRand.advance(5);
            for (Integer leave : tree.leaves) {
                if (simRand.nextInt(2) != leave) {
                    return false;
                }
            }
            return true;
        }

        private void generatestructureSeeds(long firstTreeSeed, Set<Long> seeds) {
            System.out.println("firstTreeSeed "+firstTreeSeed);
            ChunkRand rand = new ChunkRand(firstTreeSeed,false);
            //offset 1 call because Birch_other decorator has count_extra
            rand.advance(-1);

            treeToStructureSeed(rand.getSeed(), seeds);
            rand.advance(-3);
            for (int i = 0; i < 10; i++) {
                treeToStructureSeed(rand.getSeed(), seeds);
                rand.advance(-1);
            }
        }

        private void treeToStructureSeed(long treeSeed, Set<Long> seeds) {
            seeds.addAll(PopulationReverser.reverse((treeSeed^LCG.JAVA.multiplier) -80002, this.chunkX<<4, this.chunkZ<<4, new ChunkRand(), MCVersion.v1_17));
        }
    }
}
