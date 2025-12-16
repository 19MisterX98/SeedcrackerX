package kaptainwutax.seedcrackerX.cracker.bedrock;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Xoroshiro128PlusPlus;

import java.util.List;

public class OverworldCracker {

    private static final long GOLDEN_RATIO_64 = -7046029254386353131L;
    private static final long SILVER_RATIO_64 = 7640891576956012809L;

    public static void addToList(long structureSeed, List<Long> result, BedrockData data) {
        List<BlockPos> bedrock = data.getOverworld();
        BedrockType type = BedrockType.FLOOR_OVERWORLD;

        seedLoop:
        for (long i = 0; i < (1 << 16); i++) {
            long seed = i << 48 | structureSeed;

            long[] split = nextSplit(seed);

            long seedLo = split[0] ^ type.name.hashCode();
            long seedHi = split[1];

            Xoroshiro128PlusPlus rand = new Xoroshiro128PlusPlus(seedLo, seedHi);
            long bedrockLo = rand.nextLong();
            long bedrockHi = rand.nextLong();

            for (BlockPos pos : bedrock) {
                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();

                double d = Mth.map(y, type.startY, type.endY, 1.0, 0.0);

                long hash = Mth.getSeed(x, y, z);
                long posLo = bedrockLo ^ hash;
                long posHi = bedrockHi;

                Xoroshiro128PlusPlus posRand = new Xoroshiro128PlusPlus(posLo, posHi);
                float value = (float) (posRand.nextLong() >>> 40) * 5.9604645E-8F;

                if ((double) value >= d) continue seedLoop;
            }

            result.add(seed);
        }
    }

    private static long[] nextSplit(long seed) {
        long lo = seed ^ GOLDEN_RATIO_64;
        long hi = lo + SILVER_RATIO_64;

        lo = staffordMix13(lo);
        hi = staffordMix13(hi);

        return new long[]{lo, hi};
    }

    private static long staffordMix13(long value) {
        value = (value ^ value >>> 30) * -4658895280553007687L;
        value = (value ^ value >>> 27) * -7723592293110705685L;
        return value ^ value >>> 31;
    }

}
