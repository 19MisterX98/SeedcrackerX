package kaptainwutax.seedcrackerX.cracker.bedrock;

import net.minecraft.util.Mth;

public class LCGRandom {

    public static final long MULTIPLY = 25214903917L;
    public static final long ADD = 11L;
    public static final long MASK = 281474976710655L;

    private long seed;

    public LCGRandom(long seed) {
        this.setSeed(seed);
    }

    public void setSeed(long seed) {
        this.seed = (seed ^ MULTIPLY) & MASK;
    }

    public RandomSplitter nextSplitter() {
        return new RandomSplitter(this.nextLong());
    }

    public int next(int bits) {
        long m = seed * MULTIPLY + ADD & MASK;
        seed = m;

        return (int) (m >> 48 - bits);
    }

    public long nextLong() {
        int i = this.next(32);
        int j = this.next(32);
        long l = (long) i << 32;
        return l + (long) j;
    }

    public static class RandomSplitter {
        public final long seed;

        public RandomSplitter(long seed) {
            this.seed = seed;
        }

        public LCGRandom split(int x, int y, int z) {
            long l = Mth.getSeed(x, y, z);
            long m = l ^ this.seed;
            return new LCGRandom(m);
        }

        public LCGRandom split(String seed) {
            int i = seed.hashCode();

            return new LCGRandom((long) i ^ this.seed);
        }
    }

}
