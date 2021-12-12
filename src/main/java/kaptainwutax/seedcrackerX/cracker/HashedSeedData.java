package kaptainwutax.seedcrackerX.cracker;

import com.seedfinding.mccore.rand.seed.WorldSeed;
import com.seedfinding.mcseed.rand.JRand;

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
