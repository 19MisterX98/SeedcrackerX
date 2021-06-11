package kaptainwutax.seedcrackerX.cracker.decorator;

import kaptainwutax.biomeutils.biome.Biome;
import kaptainwutax.biomeutils.biome.Biomes;
import kaptainwutax.mcutils.rand.ChunkRand;
import kaptainwutax.mcutils.state.Dimension;
import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.mcutils.version.VersionMap;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.cracker.storage.TimeMachine;
import kaptainwutax.seedcrackerX.util.Log;
import kaptainwutax.seedutils.lcg.LCG;
import mjtb49.hashreversals.PopulationReverser;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.LongStream;

public class WarpedFungus extends Decorator<Decorator.Config, WarpedFungus.Data> {
    public static final VersionMap<Config> CONFIGS = new VersionMap<Decorator.Config>()
            .add(MCVersion.v1_16, new Decorator.Config(8,3));

    public WarpedFungus(MCVersion version) {
        super(CONFIGS.getAsOf(version), version);
    }

    public WarpedFungus.Data at(int blockX, int blockZ, Biome biome, List<BlockPos> posList, FullFungusData fungusData) {
        return new WarpedFungus.Data(this,blockX,blockZ,biome,posList,fungusData);
    }

    @Override
    public String getName() {
        return "warped fungus";
    }

    @Override
    public boolean canStart(WarpedFungus.Data data, long structureSeed, ChunkRand rand) {
        return true;
    }

    @Override
    public boolean isValidDimension(Dimension dimension) {
        return dimension == Dimension.NETHER;
    }

    @Override
    public Dimension getValidDimension() {
        return  Dimension.NETHER;
    }

    @Override
    public boolean isValidBiome(Biome biome) {
        return biome == Biomes.WARPED_FOREST;
    }

    public static class Data extends Decorator.Data<WarpedFungus> {

        public final FullFungusData fullFungi;
        public final List<BlockPos> posList = new ArrayList<>();
        private final int BlockX;
        private final int BlockZ;
        public Data(WarpedFungus feature, int blockX, int blockZ, Biome biome, List<BlockPos> posList, FullFungusData fungusData) {
            super(feature, blockX, blockZ, biome);
            fullFungi = fungusData;
            BlockX = blockX;
            BlockZ = blockZ;
            for (BlockPos pos : posList) {
                this.posList.add(new BlockPos(((pos.getX() % 16) + 16) % 16, 0, ((pos.getZ() % 16) + 16) % 16));
            }
        }

        public void onDataAdded(DataStorage dataStorage) {
            if(dataStorage.getTimeMachine().worldSeeds != null) return;
            if(dataStorage.getTimeMachine().structureSeeds != null && dataStorage.getTimeMachine().structureSeeds.size() == 1) return;

            Log.warn("running cracker at "+BlockX+" "+BlockZ);

            LongStream longStream = fullFungi.crackSeed();

            ArrayList<Long> longs = new ArrayList<>();
            longStream.forEach(longs::add);

            if(dataStorage.getTimeMachine().worldSeeds != null) return;
            if(dataStorage.getTimeMachine().structureSeeds != null && dataStorage.getTimeMachine().structureSeeds.size() == 1) return;

            Log.debug("====================================");
            longs.forEach(s -> {Log.printSeed("fungus seed: ${SEED}.",s);});

            if(longs.isEmpty()) {
                Log.warn("wrong fungus data.");
                Log.debug("====================================");
                return;
            }
            Log.debug("====================================");

            longs.forEach(this::reverseToDecoratorSeed);
            if(structureSeedList.isEmpty()) {
                Log.warn("no seeds found for this Fungus.");
                return;
            }

            if(dataStorage.getTimeMachine().structureSeeds == null) {
                Log.warn("got structure seeds: ");
                dataStorage.getTimeMachine().structureSeeds = new ArrayList<>();
                structureSeedList.forEach(s -> {
                    Log.printSeed("structureseed ${SEED}.",s);
                    dataStorage.getTimeMachine().structureSeeds.add(s);
                });
            } else {
                for(Long structureSeed:dataStorage.getTimeMachine().structureSeeds) {
                    if(structureSeedList.contains(structureSeed)) {
                        dataStorage.getTimeMachine().structureSeeds.clear();
                        dataStorage.getTimeMachine().structureSeeds.add(structureSeed);
                        Log.printSeed("structureseed ${SEED}.",structureSeed);
                        Log.warn("this structure seed is also usable as worldseed");
                        break;
                    }
                }
            }
            dataStorage.getTimeMachine().poke(TimeMachine.Phase.BIOMES);
        }


        private void reverseToDecoratorSeed(long fungusSeed) {
            ChunkRand rand = new ChunkRand(fungusSeed,false);
            rand.advance(-4000);
            int lastPos = -1;
            for(int i = 0; i <4000;i++) {
                int pos = rand.nextInt(16);
                for(BlockPos fungusPos:posList) {
                    if(fungusPos.getX() ==lastPos && fungusPos.getZ() == pos) {
                        rand.advance(-2);
                        if(checkPoses(rand.getSeed())) {
                            System.out.println(rand.getSeed());
                            System.out.println(i);
                            GeneratestructureSeeds(rand.getSeed());
                        }
                        rand.advance(2);
                        break;
                    }
                }
                lastPos = pos;
            }
        }

        private boolean checkPoses(long seed) {
            List<BlockPos> posesClone = new ArrayList<>(posList);
            ChunkRand rand = new ChunkRand(seed, false);
            int counter = 0;
            for (int i = 0; i < 100; i++) {
                counter++;
                if (counter > 10) {
                    return false;
                }
                int x = rand.nextInt(16);
                int z = rand.nextInt(16);
                for (int j = 0; j < posesClone.size(); j++) {
                    if (posesClone.get(j).getX() == x && posesClone.get(j).getZ() == z) {
                        counter = 0;
                        posesClone.remove(j);
                        break;
                    }
                }
                if (posesClone.isEmpty()) return true;
            }
            return false;
        }

        private final LinkedHashSet<Long> structureSeedList = new LinkedHashSet<>();

        private void GeneratestructureSeeds(long probableDecoSeed) {
            System.out.println("deco seed "+probableDecoSeed);
            ChunkRand rand = new ChunkRand(probableDecoSeed,false);
            List<Long> populationSeeds = new ArrayList<>();
            for(int i = 0; i <8; i++) {
                populationSeeds.add((rand.getSeed()^ LCG.JAVA.multiplier) -80003);
                rand.advance(-2);
            }
            for(long populationSeed:populationSeeds) {
                structureSeedList.addAll(PopulationReverser.reverse(populationSeed, BlockX, BlockZ, rand, MCVersion.v1_16_2));
            }
        }
    }
}

