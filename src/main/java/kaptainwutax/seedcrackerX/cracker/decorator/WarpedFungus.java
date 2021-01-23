package kaptainwutax.seedcrackerX.cracker.decorator;

import com.seedfinding.latticg.reversal.DynamicProgram;
import com.seedfinding.latticg.reversal.calltype.java.JavaCalls;
import com.seedfinding.latticg.util.LCG;
import kaptainwutax.biomeutils.Biome;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.cracker.storage.TimeMachine;
import kaptainwutax.seedcrackerX.util.Log;
import kaptainwutax.seedutils.mc.ChunkRand;
import kaptainwutax.seedutils.mc.Dimension;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.VersionMap;
import kaptainwutax.seedutils.mc.seed.StructureSeed;
import kaptainwutax.seedutils.mc.seed.WorldSeed;
import mjtb49.hashreversals.PopulationReverser;
import net.minecraft.util.math.BlockPos;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class WarpedFungus extends Decorator<Decorator.Config, WarpedFungus.Data> {
    public static final VersionMap<Decorator.Config> CONFIGS = new VersionMap<Decorator.Config>()
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
    public boolean isValidBiome(Biome biome) {
        return biome == Biome.WARPED_FOREST;
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

        private LinkedHashSet<Long> structureSeedList = new LinkedHashSet<>();

        private void GeneratestructureSeeds(long probableDecoSeed) {
            System.out.println("deco seed "+probableDecoSeed);
            ChunkRand rand = new ChunkRand(probableDecoSeed,false);
            List<Long> populationSeeds = new ArrayList<>();
            for(int i = 0; i <8; i++) {
                populationSeeds.add((rand.getSeed()^LCG.JAVA.multiplier) -80003);
                rand.advance(-2);
            }
            for(long populationSeed:populationSeeds) {
                structureSeedList.addAll(PopulationReverser.reverse(populationSeed, BlockX, BlockZ, rand, MCVersion.v1_16_2));
            }
        }
    }
}
/*
-240 -432
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: Populationseed predicted: 149636112698497
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: Real Populationseed: 4200046090827860341
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: Decoratorseed predicted: 149610919486313
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: nether_wastes
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]:
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: GenerationStep: 157983577057173
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]:
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: CountMultiLayer: 157983577057173
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: CountMultiLayer End: 169031210908165
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-230, y=33, z=-432}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-236, y=32, z=-432}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-225, y=84, z=-429}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-240, y=32, z=-429}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-227, y=34, z=-432}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-228, y=83, z=-426}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-227, y=83, z=-417}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-229, y=83, z=-417}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-231, y=82, z=-417}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-238, y=82, z=-417}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-232, y=34, z=-423}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-231, y=33, z=-421}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-236, y=33, z=-423}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-239, y=32, z=-424}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-237, y=32, z=-426}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: BlockPos{x=-226, y=41, z=-424}
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  169031210908165
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  94331504874167
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  149673006709824
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  149673006709824
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  194173828600607
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  221601071175728
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  221601071175728
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  240751030614193
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  104383998894144
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  241560011140570
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  78955321765150
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  184470836086137
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  264030243626129
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  17053199488173
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  172243565469685
[18:27:09] [Worker-Main-18/INFO] (Minecraft) [STDOUT]: generating fungus now  177911228238399
 */

