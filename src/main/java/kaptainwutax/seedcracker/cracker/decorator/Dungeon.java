package kaptainwutax.seedcracker.cracker.decorator;

import kaptainwutax.biomeutils.Biome;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.cracker.storage.TimeMachine;
import kaptainwutax.seedcracker.util.Log;
import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.seedutils.mc.ChunkRand;
import kaptainwutax.seedutils.mc.Dimension;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.VersionMap;
import mjtb49.hashreversals.ChunkRandomReverser;
import net.minecraft.util.math.Vec3i;
import randomreverser.call.java.FilteredSkip;
import randomreverser.call.java.NextInt;
import randomreverser.device.JavaRandomDevice;
import randomreverser.device.LCGReverserDevice;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class Dungeon extends Decorator<Decorator.Config, Dungeon.Data> {

	public static final VersionMap<Decorator.Config> CONFIGS = new VersionMap<Decorator.Config>()
			.add(MCVersion.v1_13, new Decorator.Config(2, 3))
			.add(MCVersion.v1_16, new Decorator.Config(3, 2)
									.add(3, 3, Biome.DESERT, Biome.SWAMP, Biome.SWAMP_HILLS));

	public Dungeon(MCVersion version) {
		super(CONFIGS.getAsOf(version), version);
	}

	public Dungeon(Decorator.Config config) {
		super(config, null);
	}

	@Override
	public String getName() {
		return "dungeon";
	}

	@Override
	public boolean canStart(Dungeon.Data data, long structureSeed, ChunkRand rand) {
		super.canStart(data, structureSeed, rand);

		for(int i = 0; i < 8; i++) {
			int x, y, z;

			if(this.getVersion().isOlderThan(MCVersion.v1_15)) {
				x = rand.nextInt(16);
				y = rand.nextInt(256);
				z = rand.nextInt(16);
			} else {
				x = rand.nextInt(16);
				z = rand.nextInt(16);
				y = rand.nextInt(256);
			}

			if(y == data.blockY && x == data.offsetX && z == data.offsetZ) {
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
	public boolean isValidBiome(Biome biome) {
		return biome != Biome.NETHER_WASTES && biome != Biome.SOUL_SAND_VALLEY && biome != Biome.WARPED_FOREST
					&& biome != Biome.CRIMSON_FOREST && biome != Biome.BASALT_DELTAS && biome != Biome.END_MIDLANDS
					&& biome != Biome.END_HIGHLANDS && biome != Biome.END_BARRENS && biome != Biome.SMALL_END_ISLANDS
					&& biome != Biome.THE_VOID && biome == Biome.THE_END;
	}

	public Dungeon.Data at(int blockX, int blockY, int blockZ, Vec3i size, int[] floorCalls, Biome biome) {
		return new Dungeon.Data(this, blockX, blockY, blockZ, size, floorCalls, biome);
	}

	public static class Data extends Decorator.Data<Dungeon> {
		public static final int COBBLESTONE_CALL = 0;
		public static final int MOSSY_COBBLESTONE_CALL = 1;
		public static final float MIN_FLOOR_BITS = 26.0F;
		public static final float MAX_FLOOR_BITS = 48.0F;

		public final int offsetX;
		public final int blockX;
		private final int blockY;
		public final int offsetZ;
		public final int blockZ;
		public final Vec3i size;
		public final int[] floorCalls;
		public float bitsCount;

		public Data(Dungeon feature, int blockX, int blockY, int blockZ, Vec3i size, int[] floorCalls, Biome biome) {
			super(feature, blockX >> 4, blockZ >> 4, biome);
			if(this.feature.getVersion().isOlderThan(MCVersion.v1_13)) { //1.12
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

			if(floorCalls != null) {
				for(int call: floorCalls) {
					this.bitsCount += call == COBBLESTONE_CALL ? 2.0F : 0.0F;
				}
			}
		}

		public boolean usesFloor() {
			return this.bitsCount >= MIN_FLOOR_BITS && this.bitsCount <= MAX_FLOOR_BITS;
		}

		public void onDataAdded(DataStorage dataStorage) {
			dataStorage.getTimeMachine().poke(TimeMachine.Phase.STRUCTURES);
			if(this.floorCalls == null || !this.usesFloor())return;
			if(dataStorage.getTimeMachine().worldSeeds != null) return;

			Log.warn("Short-cutting to dungeons...");

			JavaRandomDevice device = new JavaRandomDevice();

			if(this.feature.getVersion().isOlderThan(MCVersion.v1_15)) {
				device.addCall(NextInt.withValue(16, this.offsetX));
				device.addCall(NextInt.withValue(256, this.blockY));
				device.addCall(NextInt.withValue(16, this.offsetZ));
			} else {
				device.addCall(NextInt.withValue(16, this.offsetX));
				device.addCall(NextInt.withValue(16, this.offsetZ));
				device.addCall(NextInt.withValue(256, this.blockY));
			}

			device.addCall(NextInt.consume(2, 2)); //Skip size.

			for(int call: this.floorCalls) {
				if(call == COBBLESTONE_CALL) {
					device.addCall(NextInt.withValue(4, 0));
				} else if(call == MOSSY_COBBLESTONE_CALL) {
					//Skip mossy, brute-force later.
					device.addCall(FilteredSkip.filter(LCG.JAVA, r -> r.nextInt(4) != 0, 1));
				}
			}

			Set<Long> decoratorSeeds = device.streamSeeds(LCGReverserDevice.Process.EVERYTHING).sequential().limit(1).collect(Collectors.toSet());

			if(decoratorSeeds.isEmpty()) {
				Log.error("Finished dungeon search with no seeds.");
				return;
			}

			dataStorage.getTimeMachine().structureSeeds = new ArrayList<>();
			
			if(this.feature.getVersion().isOlderThan(MCVersion.v1_13)) {
				for (long decoratorSeed : decoratorSeeds) {
				
					for (int i = 0; i < 200; i++) {
						ChunkRandomReverser.reversePopulationSeed(decoratorSeed ^ LCG.JAVA.multiplier, blockX >> 4, blockZ >> 4,MCVersion.v1_12_2).forEach(s -> {
						
							//Log.printSeed("Found structure seed ${SEED}.", s);
							System.out.println("structureseed: " + s);
							dataStorage.addDungeon12StructureSeed(s);	
						});
						decoratorSeed = LCG.JAVA.combine(-1).nextSeed(decoratorSeed);
					}
				}
				if(dataStorage.getTimeMachine().worldSeeds == null) {
					Log.warn("finished structure seed search");
				}
			}else {
				LCG failedDungeon = LCG.JAVA.combine(-5);

				for(long decoratorSeed: decoratorSeeds) {
					for(int i = 0; i < 8; i++) {
						ChunkRandomReverser.reversePopulationSeed((decoratorSeed ^ LCG.JAVA.multiplier)
										- this.feature.getConfig().getSalt(this.biome),
								this.chunkX << 4, this.chunkZ << 4, SeedCracker.MC_VERSION).forEach(structureSeed -> {
							Log.printSeed("Found structure seed ${SEED}.", structureSeed);
							dataStorage.getTimeMachine().structureSeeds.add(structureSeed);
							});

						decoratorSeed = failedDungeon.nextSeed(decoratorSeed);
					}
				}
				dataStorage.getTimeMachine().poke(TimeMachine.Phase.BIOMES);
			}
		}
	}
}
