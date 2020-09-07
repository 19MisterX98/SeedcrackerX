package kaptainwutax.seedcracker.cracker;

import kaptainwutax.seedutils.lcg.rand.JRand;
import kaptainwutax.seedutils.mc.seed.WorldSeed;

public class HashedSeedData {

	private final long hashedSeed;

	public HashedSeedData(long hashedSeed) {
		this.hashedSeed = hashedSeed;
	}

	public boolean test(long seed, JRand rand) {
		return WorldSeed.toHash(seed) == this.hashedSeed;
	}

	public long getHashedSeed() {
		return this.hashedSeed;
	}

}
