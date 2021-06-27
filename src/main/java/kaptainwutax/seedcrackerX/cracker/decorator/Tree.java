package kaptainwutax.seedcrackerX.cracker.decorator;

import kaptainwutax.biomeutils.biome.Biome;
import kaptainwutax.biomeutils.biome.Biomes;
import kaptainwutax.mcutils.rand.ChunkRand;
import kaptainwutax.mcutils.state.Dimension;
import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.mcutils.version.VersionMap;
import kaptainwutax.seedcrackerX.cracker.decorator.decoratorData.TreeData;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import me.cortex.TreeCracker.program.TreeCrackerProgram;
import me.cortex.TreeCracker.trees.ICrackableTree;
import me.cortex.TreeCracker.trees.Simple116BlobTree;
import me.cortex.TreeCracker.trees.TreePos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        public Data(Tree feature, int chunkX, int chunkZ, Biome biome, List<TreeData> treeDataList) {
            super(feature, chunkX, chunkZ, biome);
            this.treeDataList = treeDataList;
        }

        public void onDataAdded(DataStorage dataStorage) {
            System.out.println("======================================");
            System.out.println("ChunkX: " + this.chunkX + " ChunkZ: " + this.chunkZ);
            treeDataList.forEach(tree -> System.out.println(tree.toString()));
            if (!alreadyRun) {
                List<Simple116BlobTree> trees = new ArrayList<>();
                treeDataList.forEach(treeData -> {
                    trees.add(new Simple116BlobTree(treeData.type, new TreePos(treeData.pos.getX() & 15, treeData.pos.getZ() & 15), treeData.height, treeData.leavesToArray()));
                });

                TreeCrackerProgram program = new TreeCrackerProgram(trees.toArray(new ICrackableTree[trees.size()]));

                try {
                    program.generateCracker().exportSource(new File(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toFile(), "out.cu"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("runnig \"compiler\" for ChunkX: " + this.chunkX + " ChunkZ: " + this.chunkZ);
                alreadyRun = true;
            }
        }
    }
}
