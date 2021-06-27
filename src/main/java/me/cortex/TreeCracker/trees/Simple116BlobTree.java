package me.cortex.TreeCracker.trees;

import me.cortex.TreeCracker.LCG.LcgTester;

public class Simple116BlobTree implements ICrackableTree {
    /*
        OAK = register("oak", Feature.TREE.configure((new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(ConfiguredFeatures.States.OAK_LOG), new SimpleBlockStateProvider(ConfiguredFeatures.States.OAK_LEAVES), new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1))).ignoreVines().build()));
        BIRCH_OTHER = register("birch_other", Feature.RANDOM_SELECTOR.configure(new RandomFeatureConfig(ImmutableList.of(BIRCH_BEES_0002.withChance(0.2F), FANCY_OAK_BEES_0002.withChance(0.1F)), OAK_BEES_0002)).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(10, 0.1F, 1))));
    */
    Simple116BlobTreeConfig config;
    TreePos pos;
    int treeHeight;
    int[] leaves;
    ITreeExtraDecorator extraDecorators;

    public Simple116BlobTree(Simple116BlobTreeConfig config, TreePos pos, int height, int[] leaves) {
        this(config, pos, height, leaves, null);
    }

    public Simple116BlobTree(Simple116BlobTreeConfig config, TreePos pos, int height, int[] leaves, ITreeExtraDecorator extraDecorators) {
        this.config = config;
        this.pos = pos;
        this.treeHeight = height;
        this.leaves = leaves;
        this.extraDecorators = extraDecorators;

        if (treeHeight < config.baseHeight || treeHeight > (config.baseHeight + config.firstRandomHeight)) {
            throw new IllegalArgumentException("Invalid tree height given");
        }

        if (leaves.length != config.mutableLeafCount) {
            throw new IllegalArgumentException("Leaf count not equal to the number of mutable leafs");
        }
    }


    @Override
    public void generateTreeTest(LcgTester treeTest) {
        config.selector.generateSelector(treeTest);

        treeTest.nextInt(config.firstRandomHeight + 1).equalTo(treeHeight - config.baseHeight);
        treeTest.advance();//This is because TrunkPlacer calls an extra random

        //In 1.16 trees generate top down meaning we gotta skip the top 4 mutable leafs that are always invalid
        treeTest.advance(4);

        for(int leafExists : leaves) {
            if (leafExists == -1) {
                treeTest.advance();
            } else {
                treeTest.nextInt(2).equalTo(leafExists);
            }
        }

        if (extraDecorators != null) {
            extraDecorators.addDecorators(treeTest);
        }
    }

    @Override
    public TreePos getTreePosition() {
        return pos;
    }

    public static final Simple116BlobTreeConfig FOREST_OAK_TREE = new Simple116BlobTreeConfig(tester -> {tester.nextFloat().greaterThanOrEqualTo(0.2F); tester.nextFloat().greaterThanOrEqualTo(0.1f);}, 4,2,12);
    public static final Simple116BlobTreeConfig FOREST_BIRCH_TREE = new Simple116BlobTreeConfig(tester -> {tester.nextFloat().lessThan(0.2F);}, 5,2,12);
    public static class Simple116BlobTreeConfig {
        ITreeTypeSelector selector;

        int baseHeight;
        int firstRandomHeight;

        int mutableLeafCount;

        public Simple116BlobTreeConfig(ITreeTypeSelector selector, int baseHeight, int firstRandomHeight, int mutableLeafCount) {
            this.selector = selector;
            this.baseHeight = baseHeight;
            this.firstRandomHeight = firstRandomHeight;
            this.mutableLeafCount = mutableLeafCount;
        }
    }
}
