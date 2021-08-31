package kaptainwutax.seedcrackerX.cracker.storage;

import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.featureutils.Feature;
import kaptainwutax.mcutils.rand.ChunkRand;
import kaptainwutax.mcutils.rand.seed.PillarSeed;
import kaptainwutax.mcutils.rand.seed.StructureSeed;
import kaptainwutax.mcutils.rand.seed.WorldSeed;
import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.BiomeData;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.util.Log;
import kaptainwutax.seedutils.lcg.LCG;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeMachine {

	public static ExecutorService SERVICE = Executors.newFixedThreadPool(5);

	private final LCG inverseLCG = LCG.JAVA.combine(-2);
	protected DataStorage dataStorage;

	public boolean isRunning = false;
	public boolean shouldTerminate = false;

	public List<Integer> pillarSeeds = null;
	public Set<Long> structureSeeds = new HashSet<>();
	public Set<Long> worldSeeds = new HashSet<>();

	public TimeMachine(DataStorage dataStorage) {
		this.dataStorage = dataStorage;
	}

	public void poke(Phase phase) {
		if (this.worldSeeds.size() == 1) return;
		this.isRunning = true;

		final Phase[] finalPhase = {phase};

		while(finalPhase[0] != null && !this.shouldTerminate) {
			if(finalPhase[0] == Phase.PILLARS) {
				if(!this.pokePillars())break;
			} else if(finalPhase[0] == Phase.STRUCTURES) {
				if(!this.pokeStructures())break;
			} else if(finalPhase[0] == Phase.BIOMES) {
				if(!this.pokeBiomes())break;
			} else if(finalPhase[0] == Phase.STRUCTURE_REDUCE) {
				if(!this.pokeStructureReduce())break;
			}

			finalPhase[0] = finalPhase[0].nextPhase();
		}
		if (this.worldSeeds.size() == 1 && !this.shouldTerminate) {
			long seed = worldSeeds.stream().findFirst().get();
			SeedCracker.entrypoints.forEach(entrypoint -> entrypoint.pushWorldSeed(seed));
		}
	}

	protected boolean pokePillars() {
		if(this.pillarSeeds != null || this.dataStorage.pillarData == null)return false;
		this.pillarSeeds = new ArrayList<>();

		Log.debug("====================================");
		Log.warn("tmachine.lookingForPillarSeed");

		for(int pillarSeed = 0; pillarSeed < 1 << 16 && !this.shouldTerminate; pillarSeed++) {
			if(this.dataStorage.pillarData.test(pillarSeed)) {
				Log.printSeed("tmachine.foundPillarSeed", pillarSeed);
				this.pillarSeeds.add(pillarSeed);
			}
		}

		if(!this.pillarSeeds.isEmpty()) {
			Log.warn("tmachine.pillarSeedSearchFinished");
		} else {
			Log.error("finishedSearchNoResult");
		}

		return true;
	}

	protected boolean pokeStructures() {
		if(this.pillarSeeds == null || !this.structureSeeds.isEmpty() ||
				this.dataStorage.getBaseBits() < this.dataStorage.getWantedBits())return false;

		Feature.Data<?>[] cache = new Feature.Data<?>[this.dataStorage.baseSeedData.size()];
		int id = 0;

		for(DataStorage.Entry<Feature.Data<?>> entry: this.dataStorage.baseSeedData) {
			cache[id++] = entry.data;
		}

		for(int pillarSeed: this.pillarSeeds) {
			Log.debug("====================================");
			Log.warn("tmachine.lookingForStructureSeeds", pillarSeed);

			AtomicInteger completion = new AtomicInteger();
			ProgressListener progressListener = new ProgressListener();

			for(int threadId = 0; threadId < 4; threadId++) {
				int fThreadId = threadId;

				SERVICE.submit(() -> {
					ChunkRand rand = new ChunkRand();

					long lower = (long)fThreadId * (1L << 30);
					long upper = (long)(fThreadId + 1) * (1L << 30);

					for(long partialWorldSeed = lower; partialWorldSeed < upper && !this.shouldTerminate; partialWorldSeed++) {
						if((partialWorldSeed & ((1 << 27) - 1)) == 0) {
							progressListener.addPercent(3.125F, true);
						}

						long seed = this.timeMachine(partialWorldSeed, pillarSeed);

						boolean matches = true;

						for(Feature.Data<?> baseSeedDatum: cache) {
							if(!baseSeedDatum.testStart(seed, rand)) {
								matches = false;
								break;
							}
						}

						if(matches) {
							this.structureSeeds.add(seed);
							Log.printSeed("foundStructureSeed", seed);
						}

					}

					completion.getAndIncrement();
				});
			}

			while(completion.get() != 4) {
				try {Thread.sleep(50);}
				catch(InterruptedException e) {e.printStackTrace();}

				if(this.shouldTerminate) {
					return false;
				}
			}

			progressListener.addPercent(0.0F, true);
		}

		if(!this.structureSeeds.isEmpty()) {
			Log.warn("tmachine.structureSeedSearchFinished");
		} else {
			Log.error("finishedSearchNoResult");
		}

		return true;
	}

	protected boolean pokeBiomes() {
		if (this.structureSeeds.isEmpty() || this.worldSeeds.size() == 1) return false;
		pokeStructureReduce();

		Log.debug("====================================");

		worldSeeds.clear();

		if(this.dataStorage.hashedSeedData != null && this.dataStorage.hashedSeedData.getHashedSeed() != 0) {
			Log.warn("tmachine.lookingForWorldSeeds");

			for(long structureSeed: this.structureSeeds) {
				WorldSeed.fromHash(structureSeed, this.dataStorage.hashedSeedData.getHashedSeed()).forEach(worldSeed -> {
					this.worldSeeds.add(worldSeed);
					Log.printSeed("tmachine.foundWorldSeed", worldSeed);
				});

				if(this.shouldTerminate) {
					return false;
				}
			}

			if(!this.worldSeeds.isEmpty()) {
				Log.warn("tmachine.worldSeedSearchFinished");
				return true;
			} else {
				Log.error("tmachine.noResultsRevertingToBiomes");
			}
		}

		this.dataStorage.biomeSeedData.dump();
		if(this.dataStorage.notEnoughBiomeData()) {
			Log.error("tmachine.moreBiomesNeeded");
			return false;
		}

		Log.warn("tmachine.lookingForWorldSeedswithBiomes", this.dataStorage.biomeSeedData.size());
		Log.warn("tmachine.fuzzyBiomeSearch");
		MCVersion version = Config.get().getVersion();
		for( long structureSeed : this.structureSeeds) {
			for (long worldSeed : StructureSeed.toRandomWorldSeeds(structureSeed)) {
				OverworldBiomeSource source = new OverworldBiomeSource(version, worldSeed);

				boolean matches = true;

				for(DataStorage.Entry<BiomeData> e: this.dataStorage.biomeSeedData) {
					if(!e.data.test(source)) {
						matches = false;
						break;
					}
				}

				if(matches) {
					this.worldSeeds.add(worldSeed);
					Log.printSeed("tmachine.foundWorldSeed", worldSeed);
				}
				if(this.shouldTerminate) {
					return false;
				}
			}
		}

		if (!this.worldSeeds.isEmpty()) return true;
		if (this.structureSeeds.size() > 10) return false;
		Log.warn("tmachine.deepBiomeSearch");
		for(long structureSeed : this.structureSeeds) {
			for(long upperBits = 0; upperBits < 1 << 16 && !this.shouldTerminate; upperBits++) {
				long worldSeed = (upperBits << 48) | structureSeed;

				OverworldBiomeSource source = new OverworldBiomeSource(version, worldSeed);

				boolean matches = true;

				for(DataStorage.Entry<BiomeData> e: this.dataStorage.biomeSeedData) {
					if(!e.data.test(source)) {
						matches = false;
						break;
					}
				}

				if(matches) {
					this.worldSeeds.add(worldSeed);
					if(this.worldSeeds.size() < 10) {
						Log.printSeed("tmachine.foundWorldSeed", worldSeed);
						if(this.worldSeeds.size() ==9) {
							Log.warn("tmachine.printSeedsInConsole");
						}
					}else {
						System.out.println("Found world seed " + worldSeed);
					}
				}

				if(this.shouldTerminate) {
					return false;
				}
			}
		}

		dispSearchEnd();

		if(!this.worldSeeds.isEmpty()) return true;

		Log.error("tmachine.deleteBiomeInformation");
		this.dataStorage.biomeSeedData.getBaseSet().clear();

		Log.warn("tmachine.randomSeedSearch");
		for (long structureSeed : this.structureSeeds) {
			StructureSeed.toRandomWorldSeeds(structureSeed).forEach(s ->
					Log.printSeed("tmachine.foundWorldSeed", s));

		}

		return true;
	}

	protected boolean pokeStructureReduce() {
		if (!this.worldSeeds.isEmpty() || this.structureSeeds.size() < 2) return false;
		if (Config.get().getVersion().isOlderThan(MCVersion.v1_13)) return false;

		Set<Long> result = new HashSet<>();
		Log.debug("====================================");
		Log.warn("tmachine.reduceSeeds");

		if (this.pillarSeeds != null) {
			structureSeeds.forEach(seed -> {
				if (this.pillarSeeds.contains((int) PillarSeed.fromStructureSeed(seed))) {
					result.add(seed);
				}
			});
		}
		if (result.size() != 1) {
			result.clear();
			this.structureSeeds.stream().filter(WorldSeed::isString).findAny().ifPresent(result::add);
		}

		if (result.size() != 1) {
			result.clear();
			this.dataStorage.baseSeedData.dump();
			Feature.Data<?>[] cache = new Feature.Data<?>[this.dataStorage.baseSeedData.size()];
			int id = 0;

			for(DataStorage.Entry<Feature.Data<?>> entry: this.dataStorage.baseSeedData) {
				cache[id++] = entry.data;
			}
			ChunkRand rand = new ChunkRand();

			for (Long seed : this.structureSeeds) {
				boolean matches = true;

				for(Feature.Data<?> baseSeedDatum: cache) {
					if(!baseSeedDatum.testStart(seed, rand)) {
						matches = false;
						break;
					}
				}

				if(matches) {
					result.add(seed);
				}
			}
		}

		if (!result.isEmpty() && this.structureSeeds.size() > result.size()) {
			if (result.size() < 10) {
				result.forEach(seed -> Log.printSeed("foundStructureSeed", seed));
			}
			this.structureSeeds = result;
			return true;
		} else {
			Log.warn("tmachine.failedReducing");
		}
		return false;
	}

	private void dispSearchEnd() {
		if(!this.worldSeeds.isEmpty()) {
			Log.warn("tmachine.worldSeedSearchFinished");
		} else {
			Log.error("finishedSearchNoResult");
		}
	}

	public long timeMachine(long partialWorldSeed, int pillarSeed) {
		long currentSeed = 0L;
		currentSeed |= (partialWorldSeed & 0xFFFF0000L) << 16;
		currentSeed |= (long)pillarSeed << 16;
		currentSeed |= partialWorldSeed & 0xFFFFL;

		currentSeed = this.inverseLCG.nextSeed(currentSeed);
		currentSeed ^= LCG.JAVA.multiplier;
		return currentSeed;
	}

	public enum Phase {
		BIOMES(null), STRUCTURES(BIOMES), PILLARS(STRUCTURES), STRUCTURE_REDUCE(null);

		private final Phase nextPhase;

		Phase(Phase nextPhase) {
			this.nextPhase = nextPhase;
		}

		public Phase nextPhase() {
			return this.nextPhase;
		}
	}

}
