package kaptainwutax.seedcrackerX.cracker.decorator;


import com.seedfinding.latticg.reversal.DynamicProgram;
import com.seedfinding.latticg.reversal.calltype.java.JavaCalls;
import com.seedfinding.latticg.util.LCG;
import com.seedfinding.mcbiome.biome.Biome;
import com.seedfinding.mcbiome.biome.Biomes;
import com.seedfinding.mccore.rand.ChunkRand;
import com.seedfinding.mccore.state.Dimension;
import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mccore.version.VersionMap;
import com.seedfinding.mcreversal.ChunkRandomReverser;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.cracker.storage.TimeMachine;
import kaptainwutax.seedcrackerX.util.HeightContext;
import kaptainwutax.seedcrackerX.util.Log;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.WorldgenRandom;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Dungeon extends Decorator<Decorator.Config, Dungeon.Data> {

    public static final VersionMap<Config> CONFIGS = new VersionMap<Decorator.Config>()
            .add(MCVersion.v1_13, new Decorator.Config(2, 3))
            .add(MCVersion.v1_16, new Decorator.Config(3, 2)
                    .add(3, 3, Biomes.DESERT, Biomes.SWAMP, Biomes.SWAMP_HILLS))
            .add(MCVersion.v1_18, new Decorator.Config(3, 2));

    public Dungeon(MCVersion version) {
        super(CONFIGS.getAsOf(version), version);
    }

    @Override
    public String getName() {
        return "dungeon";
    }

    @Override
    public boolean canStart(Dungeon.Data data, long structureSeed, ChunkRand rand) {
        super.canStart(data, structureSeed, rand);

        for (int i = 0; i < 8; i++) {
            int x, y, z;

            if (this.getVersion().isOlderThan(MCVersion.v1_15)) {
                x = rand.nextInt(16);
                y = rand.nextInt(256);
                z = rand.nextInt(16);
            } else {
                x = rand.nextInt(16);
                z = rand.nextInt(16);
                y = rand.nextInt(data.heightContext.getHeight()) + data.heightContext.getBottomY();
            }

            if (y == data.blockY && x == data.offsetX && z == data.offsetZ) {
                return true;
            }

            rand.nextInt(2);
            rand.nextInt(2);
        }

        return false;
    }

    @Override
    public boolean canStart(Dungeon.Data data, long worldSeed, WorldgenRandom rand) {
        super.canStart(data, worldSeed, rand);
        int x, y, z;

        for (int i = 0; i < 10; i++) {

            x = rand.nextInt(16);
            z = rand.nextInt(16);
            y = rand.nextInt(320);

            if (y == data.blockY && x == data.offsetX && z == data.offsetZ) {
                return true;
            }

            rand.nextInt(2);
            rand.nextInt(2);
        }
        return false;
    }

    @Override
    public boolean isValidDimension(Dimension dimension) {
        return dimension == Dimension.OVERWORLD;
    }

    @Override
    public Dimension getValidDimension() {
        return Dimension.OVERWORLD;
    }

    @Override
    public boolean isValidBiome(Biome biome) {
        return biome != Biomes.NETHER_WASTES && biome != Biomes.SOUL_SAND_VALLEY && biome != Biomes.WARPED_FOREST
                && biome != Biomes.CRIMSON_FOREST && biome != Biomes.BASALT_DELTAS && biome != Biomes.END_MIDLANDS
                && biome != Biomes.END_HIGHLANDS && biome != Biomes.END_BARRENS && biome != Biomes.SMALL_END_ISLANDS
                && biome != Biomes.THE_VOID && biome == Biomes.THE_END;
    }

    public Dungeon.Data at(int blockX, int blockY, int blockZ, Vec3i size, int[] floorCalls, Biome biome, HeightContext heightContext) {
        return new Dungeon.Data(this, blockX, blockY, blockZ, size, floorCalls, biome, heightContext);
    }

    public static class Data extends Decorator.Data<Dungeon> {
        public static final int COBBLESTONE_CALL = 0;
        public static final int MOSSY_COBBLESTONE_CALL = 1;
        public static final float MIN_FLOOR_BITS = 26.0F;
        public static final float MAX_FLOOR_BITS = 48.0F;

        public final int offsetX;
        public final int blockX;
        public final int offsetZ;
        public final int blockZ;
        public final Vec3i size;
        public final int[] floorCalls;
        private final int blockY;
        public float bitsCount;
        public HeightContext heightContext;

        public Data(Dungeon feature, int blockX, int blockY, int blockZ, Vec3i size, int[] floorCalls, Biome biome, HeightContext heightContext) {
            super(feature, blockX >> 4, blockZ >> 4, biome);
            if (this.feature.getVersion().isOlderThan(MCVersion.v1_13)) { //1.12
                blockX -= 8;
                blockZ -= 8;
            }
            this.offsetX = blockX & 15;
            this.blockY = blockY;
            this.offsetZ = blockZ & 15;
            this.size = size;
            this.floorCalls = floorCalls;
            this.blockX = blockX;
            this.blockZ = blockZ;
            this.heightContext = heightContext;

            if (floorCalls != null) {
                for (int call : floorCalls) {
                    this.bitsCount += call == COBBLESTONE_CALL ? 2.0F : 0.0F;
                }
            }
        }

        public boolean usesFloor() {
            return this.bitsCount >= MIN_FLOOR_BITS && this.bitsCount <= MAX_FLOOR_BITS;
        }

        public void onDataAdded(DataStorage dataStorage) {
            dataStorage.getTimeMachine().poke(TimeMachine.Phase.STRUCTURES);
            if (dataStorage.getTimeMachine().shouldTerminate) return;
            if (dataStorage.getTimeMachine().worldSeeds.size() == 1) return;
            if (dataStorage.getTimeMachine().structureSeeds.size() == 1) return;
            if (this.feature.getVersion().isNewerThan(MCVersion.v1_17_1)) return;
            if (this.floorCalls == null || !this.usesFloor()) return;


            Log.warn("dungeon.start");
            boolean debug = kaptainwutax.seedcrackerX.config.Config.get().debug;
            if (debug) {
                StringBuilder floorString = new StringBuilder();
                for (int floorCall : this.floorCalls) {
                    floorString.append(floorCall);
                }
                Log.printDungeonInfo(this.blockX + ", " + this.blockY + ", " + this.blockZ + ", \"" + floorString + "\"");
                Log.warn("Dungeonbiome: " + this.biome.getName());
            }
            DynamicProgram device = DynamicProgram.create(LCG.JAVA);

            if (this.feature.getVersion().isOlderThan(MCVersion.v1_15)) {
                device.add(JavaCalls.nextInt(16).equalTo(this.offsetX));
                device.add(JavaCalls.nextInt(256).equalTo(this.blockY));
                device.add(JavaCalls.nextInt(16).equalTo(this.offsetZ));
            } else {
                device.add(JavaCalls.nextInt(16).equalTo(this.offsetX));
                device.add(JavaCalls.nextInt(16).equalTo(this.offsetZ));
                device.add(JavaCalls.nextInt(heightContext.getHeight()).equalTo(heightContext.getDistanceToBottom(this.blockY)));
            }
            device.skip(2);

            for (int call : this.floorCalls) {
                if (call == COBBLESTONE_CALL) {
                    device.add(JavaCalls.nextInt(4).equalTo(0));
                } else if (call == MOSSY_COBBLESTONE_CALL) {
                    //Skip mossy, brute-force later.
                    device.filteredSkip(r -> {
                        int l = r.nextInt(4);
                        return l != 0;
                    }, 1);
                } else if (call == 2) {
                    device.skip(1);
                }
            }

            Set<Long> decoratorSeeds = device.reverse().parallel().boxed().collect(Collectors.toSet());

            if (dataStorage.getTimeMachine().shouldTerminate) {
                return;
            }

            if (decoratorSeeds.isEmpty()) {
                Log.error("dungeon.structureSeedSearchFailed");
                return;
            }
            if (debug) {
                for (long decoratorSeed : decoratorSeeds) {
                    Log.warn("Dungeonseed: " + decoratorSeed);
                }
            }

            Set<Long> result = new HashSet<>();

            if (this.feature.getVersion().isOlderThan(MCVersion.v1_13)) {
                for (long decoratorSeed : decoratorSeeds) {

                    for (int i = 0; i < 200; i++) {
                        for (long structureSeed : ChunkRandomReverser.reversePopulationSeed(decoratorSeed ^ LCG.JAVA.multiplier, blockX >> 4, blockZ >> 4, MCVersion.v1_12_2)) {
                            if (!dataStorage.getTimeMachine().structureSeeds.add(structureSeed)) {
                                result.add(structureSeed);
                            }
                        }
                        decoratorSeed = LCG.JAVA.combine(-1).nextSeed(decoratorSeed);
                    }
                }
                if (result.isEmpty()) {
                    Log.warn("dungeon.finishedNeedAnotherOne");
                } else if (result.size() == 1) {
                    result.forEach(seed -> Log.printSeed("foundStructureSeed", seed));
                    dataStorage.getTimeMachine().structureSeeds = result;
                    dataStorage.getTimeMachine().poke(TimeMachine.Phase.BIOMES);
                } else {
                    Log.warn("wtf, this message means something isnt working as it should");
                }
            } else {
                LCG failedDungeon = LCG.JAVA.combine(-5);


                for (long decoratorSeed : decoratorSeeds) {
                    for (int i = 0; i < 8; i++) {
                        ChunkRandomReverser.reversePopulationSeed((decoratorSeed ^ LCG.JAVA.multiplier)
                                        - this.feature.getConfig().getSalt(this.biome),
                                this.chunkX << 4, this.chunkZ << 4, this.feature.getVersion()).forEach(structureSeed -> {
                            Log.printSeed("foundStructureSeed", structureSeed);
                            if (!dataStorage.getTimeMachine().structureSeeds.add(structureSeed)) {
                                result.add(structureSeed);
                            }
                        });

                        decoratorSeed = failedDungeon.nextSeed(decoratorSeed);
                    }
                }
                if (result.size() == 1) {
                    result.forEach(seed -> Log.printSeed("crossCompare", seed));
                    dataStorage.getTimeMachine().structureSeeds = result;
                }
                dataStorage.getTimeMachine().poke(TimeMachine.Phase.BIOMES);
            }
        }
    }
}
