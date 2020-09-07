package kaptainwutax.seedcracker.cracker.decorator;

import kaptainwutax.biomeutils.Biome;
import kaptainwutax.biomeutils.source.BiomeSource;
import kaptainwutax.featureutils.Feature;
import kaptainwutax.seedutils.mc.ChunkRand;
import kaptainwutax.seedutils.mc.MCVersion;

import java.util.HashMap;
import java.util.Map;

public abstract class Decorator<C extends Decorator.Config, D extends Decorator.Data<?>> extends Feature<C, D> {

	public Decorator(C config, MCVersion version) {
		super(config, version);
	}

	public int getIndex(Biome biome) {
		return this.getConfig().getSalt(biome) % 10000;
	}

	public int getStep(Biome biome) {
		return this.getConfig().getSalt(biome) / 10000;
	}

	@Override
	public boolean canStart(D data, long structureSeed, ChunkRand rand) {
		rand.setDecoratorSeed(structureSeed, data.chunkX << 4, data.chunkZ << 4,
				this.getIndex(data.biome), this.getStep(data.biome), this.getVersion());
		return true;
	}

	@Override
	public final boolean canSpawn(D data, BiomeSource source) {
		return this.canSpawn(data.chunkX, data.chunkZ, source);
	}

	public boolean canSpawn(int chunkX, int chunkZ, BiomeSource source) {
		if(this.getVersion().isOlderThan(MCVersion.v1_16)) {
			return this.isValidBiome(source.getBiome((chunkX << 4) + 8, 0, (chunkZ << 4) + 8));
		}

		return this.isValidBiome(source.getBiomeForNoiseGen((chunkX << 2) + 2, 0, (chunkZ << 2) + 2));
	}

	public abstract boolean isValidBiome(Biome biome);

	public static class Config extends Feature.Config {
		public final int defaultSalt;
		public final Map<Biome, Integer> salts = new HashMap<>();

		public Config(int step, int index) {
			this.defaultSalt = step * 10000 + index;
		}

		public Config add(int step, int index, Biome... biomes) {
			for(Biome biome: biomes) {
				this.salts.put(biome, step * 10000 + index);
			}

			return this;
		}

		public int getSalt(Biome biome) {
			return this.salts.getOrDefault(biome, this.defaultSalt);
		}
	}

	public static class Data<T extends Decorator<?, ?>> extends Feature.Data<T> {
		public final Biome biome;

		public Data(T feature, int chunkX, int chunkZ, Biome biome) {
			super(feature, chunkX, chunkZ);
			this.biome = biome;
		}
	}

}
