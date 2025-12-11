package kaptainwutax.seedcrackerX.cracker.bedrock;

import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

import static kaptainwutax.seedcrackerX.cracker.bedrock.LCGRandom.MASK;

public class BedrockHelper {

    public static boolean performCheck(int x, int y, int z, LCGRandom.RandomSplitter splitter) {
        BedrockType type;
        try {
            type = BedrockType.getFromY(y);
        } catch (IllegalArgumentException ignored) {
            return false;
        }

        return performCheck(x, y, z, splitter, type);
    }

    public static boolean performCheck(int x, int y, int z, LCGRandom.RandomSplitter splitter, BedrockType bedrockType) {
        int desiredInt = getDesiredIntFromYHeight(y);

        int value = splitter.split(x, y, z).next(24);

        if (bedrockType.invert) {
            return value >= desiredInt;
        }

        return value < desiredInt;
    }

    public static int getDesiredIntFromYHeight(int y) {
        BedrockType bedrockType = BedrockType.getFromY(y);

        if (bedrockType.invert) {
            return (int) ((1 - Mth.map(y, bedrockType.endY - 1, bedrockType.startY - 1, 1, 0)) * (1 << 24));
        }

        return (int) (Mth.map(y, bedrockType.startY, bedrockType.endY, 1, 0) * (1 << 24));
    }

    public static long structureSeedToBedrockSeed(long structureSeed, String ruleName) {
        checkValidBedrockRule(ruleName);

        LCGRandom.RandomSplitter random = new LCGRandom(structureSeed).nextSplitter();
        LCGRandom rand = random.split(ruleName);
        LCGRandom.RandomSplitter bedrockRand = rand.nextSplitter();

        return bedrockRand.seed;
    }

    public static List<Long> bedrockSeedToStructureSeed(long bedrockSeed, String ruleName) {
        checkValidBedrockRule(ruleName);

        bedrockSeed &= MASK;

        List<Long> result = new ArrayList<>();

        for (long l : getSeedFromMaskedNextLong(bedrockSeed)) {
            l ^= ruleName.hashCode();

            result.addAll(getSeedFromMaskedNextLong(l));
        }

        return result;
    }

    private static void checkValidBedrockRule(String ruleName) {
        if (!ruleName.equals("minecraft:bedrock_roof") && !ruleName.equals("minecraft:bedrock_floor")) {
            throw new IllegalArgumentException("not a valid rule! {" + ruleName + "}");
        }
    }

    public static List<Long> getSeedFromMaskedNextLong(long structureSeed) {
        List<Long> res = new ArrayList<>(2);
        structureSeed = structureSeed & 0xffff_ffff_ffffL;
        long lowerBits = (structureSeed & 0xffff_ffffL);
        long upperBits = ((structureSeed) >> 32);

        if ((lowerBits & 0x8000_0000L) != 0) {
            upperBits += 1;
        }

        int bitsOfDanger = 1;

        long lowMin = (lowerBits << 16 - bitsOfDanger);
        long lowMax = (((lowerBits + 1) << 16 - bitsOfDanger) - 1);
        long upperMin = (((upperBits << 16) - 107048004364969L) >> bitsOfDanger);

        long m1lv = floorDiv(lowMax * -33441L + upperMin * 17549L, 1L << 31 - bitsOfDanger) + 1;
        long m2lv = floorDiv(lowMin * 46603L + upperMin * 39761L, 1L << 32 - bitsOfDanger) + 1;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                long seed = (-39761L * (m1lv + i)) + 35098L * (m2lv + j);
                if (((46603L * (m1lv + i)) + 66882L * (m2lv + j) + 107048004364969L) >> 16 == upperBits) {
                    if ((seed) >> 16 == lowerBits) {
                        res.add(((seed * 254681119335897L + 120305458776662L) & 0xffff_ffff_ffffL) ^ LCGRandom.MULTIPLY);
                    }
                }
            }
        }

        return res;
    }

    private static long floorDiv(long a, long b) {
        return (a - (a < 0 ? b - 1 : 0)) / b;
    }

}
