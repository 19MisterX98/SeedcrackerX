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
        private int[][] fungiPoses;
        public Data(WarpedFungus feature, int blockX, int blockZ, Biome biome, List<BlockPos> posList, FullFungusData fungusData) {
            super(feature, blockX % 16, blockZ % 16, biome);
            fullFungi = fungusData;
            this.posList.addAll(posList);
            fungiPoses = new int[posList.size()][2];
            int counter = 0;
            for (BlockPos pos:posList) {
                fungiPoses[counter][0] =pos.getX();
                fungiPoses[counter][1] =pos.getZ();
                counter++;
            }
            BlockX = blockX;
            BlockZ = blockZ;
        }
        public ChunkRand rand = new ChunkRand(224974476960896L,false);

        public void onDataAdded(DataStorage dataStorage) {
            if(dataStorage.getTimeMachine().worldSeeds != null) return;


            int hoehe = fullFungi.height;
            boolean breit = fullFungi.big;
            int hatSizeVine = fullFungi.vineLayerSize;
            int[] vine = fullFungi.vines.stream().mapToInt(i ->i).toArray();

            int[] layerSizes = fullFungi.layerSizes.stream().mapToInt(i -> i).toArray();

            int[][][] bottomLayer = fullFungi.layers;

            ArrayList<Integer> trunkdata = new ArrayList<>(fullFungi.bigtrunkData);


            //System.out.println("running cracker at "+fungiPoses[0][0]+" "+fungiPoses[0][1]);
            Log.warn("running cracker at "+fungiPoses[0][0]+" "+fungiPoses[0][1]);



            for (int i = 0; i < layerSizes.length;i++) {
                for (int x = 0; x <= layerSizes[i]*2;x++) {
                    String printer = "";
                    for (int z = 0; z <= layerSizes[i]*2;z++) {
                        printer += bottomLayer[i][z][x];
                    }
                    System.out.println(printer);
                }
                System.out.println("========================");
            }


            int doppelt = 0;
            if(hoehe > 7 && hoehe % 2 == 0) {
                doppelt = 1;
                if(hoehe > 13){
                    doppelt = 2;
                }
            }

            DynamicProgram dynamicProgram = DynamicProgram.create(LCG.JAVA);
            if(doppelt < 2) {
                dynamicProgram.skip(2);
                rand.advance(2);
                //dynamicProgram.add(JavaCalls.nextInt(12).greaterThan(0));
                //dynamicProgram.add(JavaCalls.nextInt(10).equalTo(hoehe - 4));
            } else {
                //dynamicProgram.add(JavaCalls.nextInt(10).equalTo((hoehe/2) -4));
                dynamicProgram.skip(1);
                dynamicProgram.add(JavaCalls.nextInt(12).equalTo(0));
                rand.advance(1);
                System.out.println("next int 12"+(rand.nextInt(12) == 0));
            }
            if(breit){
                dynamicProgram.add(JavaCalls.nextFloat().lessThan(0.06F));
                System.out.println(rand.nextFloat()<0.06F);
            } else {
                dynamicProgram.skip(1);
                rand.advance(1);
                //dynamicProgram.add(JavaCalls.nextFloat().greaterOrEqual(0.06F));
            }

            if(breit) {
                for (int blockdata:trunkdata) {
                    if(blockdata == 0) {
                        System.out.println("nextfloat >0.1" +(rand.nextFloat()>=0.1F));
                        dynamicProgram.skip(1);
                    } else if (blockdata == 1) {
                        System.out.println("nextfloat <0.1 " + (rand.nextFloat()<0.1F));
                        dynamicProgram.add(JavaCalls.nextFloat().lessThan(0.1F));
                    } else {
                        System.out.println("this shouldnt be possible, but just in case here is your error message");
                    }
                    System.out.println(blockdata);
                }
            }

            dynamicProgram.skip(2);
            rand.advance(2);

            ArrayList<Integer> done = new ArrayList<>();
            for (int j= 3;j > 0;j--) {

                int count = 0;

                for (int i = 0; i < hatSizeVine*8; i++) {
                    if(vine[i] == 0 && !done.contains(i)) {
                        dynamicProgram.skip(1);
                        rand.advance(1);

                    } else if (vine[i] == j) {
                        done.add(i);
                        dynamicProgram.add(JavaCalls.nextFloat().lessThan(0.15F));
                        System.out.println("nextfloat < 0.15"+(rand.nextFloat() < 0.15F));

                    }else if(!done.contains(i)) {
                        count++;
                        dynamicProgram.skip(1);
                        rand.advance(1);
                    }
                }
                dynamicProgram.skip(1);
                rand.advance(1);
            }
            int relativePos;
            int blockType;
            int layer = 0;

            for(int size:layerSizes) {
                size *=2;
                //System.out.println("LayerSize "+size);

                for(int x = 0; x <= size; x++) {

                    boolean siteX = x == 0 || x == size;
                    for (int z = 0; z <= size; z++) {

                        //System.out.println(x+" "+z);

                        boolean siteZ = z == 0 || z == size;

                        relativePos = (siteX ? 1:0) + (siteZ ? 1:0);

                        blockType = bottomLayer[layer][x][z];

                        generateBlock(relativePos,blockType,dynamicProgram,rand);
                    }
                }

                dynamicProgram.skip(1);
                rand.advance(1);
                layer++;
            }



            LongStream longStream = dynamicProgram.reverse();

            ArrayList<Long> longs = new ArrayList<>();
            longStream.forEach(longs::add);
            Log.debug("====================================");
            longs.forEach(s -> {Log.printSeed("fungus seed: ${SEED}.",s);});

            if(longs.isEmpty()) {
                System.out.println("tree data wrong");
                Log.warn("wrong fungus data.");
                return;
            }
            Log.debug("====================================");

            longs.forEach(this::reverseToDecoratorSeed);
            if(structureSeedList.isEmpty()) {
                Log.warn("no seeds found for this Fungus.");
                return;
            }

            Log.warn("got structure seeds: ");
            dataStorage.getTimeMachine().structureSeeds = new ArrayList<>();
            structureSeedList.forEach(s -> {
                Log.printSeed("structureseed ${SEED}.",s);
                dataStorage.getTimeMachine().structureSeeds.add(s);
            });
            dataStorage.getTimeMachine().poke(TimeMachine.Phase.BIOMES);
        }


        private void reverseToDecoratorSeed(long fungusSeed) {

            List<List<Integer>> fungusPosess = new ArrayList<>();
            for (int[] fungiPose : fungiPoses) {
                List<Integer> ints = new ArrayList<>();
                ints.add(((fungiPose[0] % 16) + 16) % 16);
                ints.add(((fungiPose[1] % 16) + 16) % 16);
                fungusPosess.add(ints);
            }
            ChunkRand rand = new ChunkRand(fungusSeed,false);
            rand.advance(-4000);
            int lastPos = -1;
            for(int i = 0; i <4000;i++) {
                int pos = rand.nextInt(16);
                for(List<Integer> fungusPoss:fungusPosess) {
                    if(fungusPoss.get(0)==lastPos && fungusPoss.get(1) == pos) {
                        rand.advance(-2);
                        List<List<Integer>> fungusCopy = new ArrayList<>(fungusPosess);
                        if(checkPoses(rand.getSeed(),fungusCopy)) {
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

        private boolean checkPoses(long seed, List<List<Integer>> posesClone) {
            ChunkRand rand = new ChunkRand(seed,false);
            int counter = 0;
            for (int i = 0; i <100; i++) {
                counter++;
                if(counter > 10) {
                    return false;
                }
                int x = rand.nextInt(16);
                int z = rand.nextInt(16);
                for(int j = 0; j < posesClone.size();j++) {
                    List<Integer> pos = posesClone.get(j);
                    if(pos.get(0) == x && pos.get(1) ==z) {
                        counter = 0;
                        posesClone.remove(j);
                        break;
                    }
                }
                if(posesClone.isEmpty()) return true;
            }
            return false;
        }

        private LinkedHashSet<Long> structureSeedList = new LinkedHashSet<>();

        private void GeneratestructureSeeds(long probableDecoSeed) {
            System.out.println("deco seed "+probableDecoSeed);
            ChunkRand rand = new ChunkRand(probableDecoSeed,false);
            List<Long> populationSeeds = new ArrayList<>();
            for(int i = 0; i <5; i++) {
                populationSeeds.add((rand.getSeed()^LCG.JAVA.multiplier) -80003);
                rand.advance(-2);
            }
            for(long populationSeed:populationSeeds) {
                System.out.println("populationseed: "+populationSeed);
                structureSeedList.addAll(PopulationReverser.reverse(populationSeed, BlockX, BlockZ, rand, MCVersion.v1_16_2));
            }
        }

        private void generateBlock(int relativePos, int blockType, DynamicProgram dynamicProgram, ChunkRand rand) {
            //System.out.println("Block: "+blockType +" "+"pos: "+relativePos);
            if(blockType == 3) return;
            switch (relativePos) {
                case 0:
                    //Inside
                    switch (blockType) {
                        case 0:
                            dynamicProgram.skip(2);
                            rand.advance(2);
                            break;
                        case 1:
                            dynamicProgram.skip(3);
                            rand.advance(3);
                            //dynamicProgram.add(JavaCalls.nextFloat().lessThan(0.2F));
                            //System.out.println("nextFloat < 0.2F"+(rand.nextFloat() < 0.2F));
                            //dynamicProgram.skip(1);
                            //rand.advance(1);
                            break;
                        case 2:
                            dynamicProgram.add(JavaCalls.nextFloat().lessThan(0.1F));
                            System.out.println("nextFloat < 0.1F"+(rand.nextFloat() < 0.1F));
                    }
                    break;
                case 1:
                    //Wall
                    switch (blockType) {
                        case 0:
                            dynamicProgram.skip(1);
                            dynamicProgram.add(JavaCalls.nextFloat().greaterThanEqual(0.98F));
                            rand.advance(1);
                            System.out.println("nextFloat > 0.98F"+(rand.nextFloat() >= 0.98));
                            break;
                        case 1:
                            rand.advance(3);
                            dynamicProgram.skip(3);
                            break;
                        case 2:
                            dynamicProgram.add(JavaCalls.nextFloat().lessThan(5.0E-4F));
                            System.out.println("nextFloat < 5.0E-4F"+(rand.nextFloat() <5.0E-4F));
                    }
                    break;
                case 2:
                    //Corner
                    switch (blockType) {
                        case 0:
                            rand.advance(2);
                            dynamicProgram.skip(2);
                            break;
                        case 1:
                            dynamicProgram.skip(3);
                            rand.advance(3);
                            break;
                        case 2:

                            dynamicProgram.add(JavaCalls.nextFloat().lessThan(0.01F));
                            System.out.println("nextFloat < 0.01F"+(rand.nextFloat() < 0.01F));
                    }
                    break;
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

