package kaptainwutax.seedcrackerX.cracker.bedrock;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

import static kaptainwutax.seedcrackerX.cracker.bedrock.LCGRandom.MASK;
import static kaptainwutax.seedcrackerX.cracker.bedrock.LCGRandom.MULTIPLY;

abstract class AbstractNetherCracker {

    protected final BedrockData bedrockData;

    protected AbstractNetherCracker(BedrockData bedrockData) {
        this.bedrockData = bedrockData;
    }

    public List<Long> crack() {
        List<Test> tests = loadTests();

        List<Test> mainTests = new ArrayList<>(tests);

        mainTests.removeIf(test -> test.y < 64);
        mainTests = mainTests.subList(0, Math.min(20, mainTests.size()));

        Test[] testArr = mainTests.toArray(new Test[0]);

        List<Long> results = getSeedCandidates(testArr);

        List<Long> structureSeeds = new ArrayList<>();

        for (long l : results) {
            structureSeeds.addAll(BedrockHelper.bedrockSeedToStructureSeed(l, "minecraft:bedrock_roof"));
        }

        structureSeeds.removeIf(l -> {
            for (Test test : tests) {
                long s = BedrockHelper.structureSeedToBedrockSeed(l, BedrockType.getFromY(test.y).name);
                LCGRandom.RandomSplitter splitter = new LCGRandom.RandomSplitter(s);

                if (!BedrockHelper.performCheck(test.x, test.y, test.z, splitter)) return true;
            }

            return false;
        });

        return structureSeeds;
    }

    protected abstract List<Long> getSeedCandidates(Test[] testArr);

    protected void runChecks(Test[] tests, long upperBits, int unknownBits, List<Long> resultList) {
        long lowerBitMask = (1L << unknownBits) - 1;
        long sub = lowerBitMask * MULTIPLY;
        long hashMask = MASK ^ lowerBitMask;

        for (Test test : tests) {
            if ((((upperBits ^ test.hashXor & hashMask) * MULTIPLY + test.offset) & MASK) < test.add - sub) return;
        }

        if (unknownBits == 0) {
            resultList.add(upperBits);
            return;
        }

        unknownBits--;

        long split = 1L << unknownBits;

        runChecks(tests, upperBits, unknownBits, resultList);
        runChecks(tests, upperBits | split, unknownBits, resultList);
    }

    protected long[] getBounds(int y) {
        double lowerBound = 0;
        double upperBound = 1;

        if (y > 5) {
            y -= 122;
            lowerBound = (5 - y) / 5d;
        } else {
            upperBound = (5 - y) / 5d;
        }

        lowerBound *= MASK;
        upperBound *= MASK;

        return new long[]{(long) lowerBound, (long) upperBound};
    }

    protected List<Test> loadTests() {
        List<BlockPos> netherBedrock = new ArrayList<>();
        netherBedrock.addAll(bedrockData.getNetherFloor());
        netherBedrock.addAll(bedrockData.getNetherRoof());

        List<Test> tests = new ArrayList<>();

        for (BlockPos pos : netherBedrock) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            int lowerThan = BedrockHelper.getDesiredIntFromYHeight(y);

            tests.add(new Test(Mth.getSeed(x, y, z), x, y, z, lowerThan, getBounds(y)[0], getBounds(y)[1],
                    Mth.getSeed(x, y, z) ^ MULTIPLY, MASK - getBounds(y)[1], MASK - getBounds(y)[1] + getBounds(y)[0]));
        }

        return tests;
    }

    protected record Test(long hash, int x, int y, int z, int lowerThan, long lowerBound, long upperBound,
                          long hashXor, long offset, long add) {
    }

}
