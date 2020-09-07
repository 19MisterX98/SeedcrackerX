package kaptainwutax.seedcracker.cracker.decorator;

import kaptainwutax.biomeutils.Biome;
import kaptainwutax.seedutils.mc.ChunkRand;
import kaptainwutax.seedutils.mc.Dimension;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.VersionMap;

public class EmeraldOre extends Decorator<Decorator.Config, EmeraldOre.Data> {

	public static final VersionMap<Decorator.Config> CONFIGS = new VersionMap<Config>()
			.add(MCVersion.v1_13, new Decorator.Config(4, 14))
			.add(MCVersion.v1_16, new EmeraldOre.Config(6, 14));

	public EmeraldOre(MCVersion version) {
		super(CONFIGS.getAsOf(version), version);
	}

	public EmeraldOre(Decorator.Config config) {
		super(config, null);
	}

	@Override
	public String getName() {
		return "emerald_ore";
	}

	@Override
	public boolean canStart(Data data, long structureSeed, ChunkRand rand) {
		super.canStart(data, structureSeed, rand);

		int bound = rand.nextInt(6) + 3;

		for(int i = 0; i < bound; i++) {
			int x, y, z;

			if(this.getVersion().isOlderThan(MCVersion.v1_15)) {
				x = rand.nextInt(16);
				y = rand.nextInt(28) + 4;
				z = rand.nextInt(16);
			} else {
				x = rand.nextInt(16);
				z = rand.nextInt(16);
				y = rand.nextInt(28) + 4;
			}

			if(y == data.blockY && x == data.offsetX && z == data.offsetZ) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isValidDimension(Dimension dimension) {
		return dimension == Dimension.OVERWORLD;
	}

	@Override
	public boolean isValidBiome(Biome biome) {
		return biome == Biome.GRAVELLY_MOUNTAINS || biome == Biome.MODIFIED_GRAVELLY_MOUNTAINS
				|| biome == Biome.MOUNTAINS || biome == Biome.WOODED_MOUNTAINS || biome == Biome.MOUNTAIN_EDGE;
	}

	public EmeraldOre.Data at(int blockX, int blockY, int blockZ, Biome biome) {
		return new EmeraldOre.Data(this, blockX, blockY, blockZ, biome);
	}

	public static class Data extends Decorator.Data<EmeraldOre> {
		public final int offsetX;
		public final int blockY;
		public final int offsetZ;

		public Data(EmeraldOre feature, int blockX, int blockY, int blockZ, Biome biome) {
			super(feature, blockX >> 4, blockZ >> 4, biome);
			this.offsetX = blockX & 15;
			this.blockY = blockY;
			this.offsetZ = blockZ & 15;
		}
	}

}
