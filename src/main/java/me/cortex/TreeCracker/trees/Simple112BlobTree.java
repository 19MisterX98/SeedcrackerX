package me.cortex.TreeCracker.trees;

import me.cortex.TreeCracker.LCG.LcgTester;

public class Simple112BlobTree implements ICrackableTree {

    Simple112BlobTreeConfig config;
    TreePos pos;
    int treeHeight;
    int[] leaves;


    public Simple112BlobTree(Simple112BlobTreeConfig config, TreePos pos, int height, int[] leaves) {
        this.config = config;
        this.pos = pos;
        this.treeHeight = height;
        this.leaves = leaves;

        if (treeHeight < config.baseHeight || treeHeight > (config.baseHeight + config.firstRandomHeight)) {
            throw new IllegalArgumentException("Invalid tree height given for tree "+pos);
        }

        if (leaves.length != config.mutableLeafCount) {
            throw new IllegalArgumentException("Leaf count not equal to the number of mutable leafs");
        }
    }


    @Override
    public void generateTreeTest(LcgTester treeTest) {
        config.selector.generateSelector(treeTest);
        treeTest.nextInt(config.firstRandomHeight + 1).equalTo(treeHeight - config.baseHeight);

        for(int leafExists : leaves) {
            if (leafExists == -1) {
                treeTest.advance();
            } else {
                treeTest.nextInt(2).equalTo(leafExists);
            }
        }
    }

    @Override
    public TreePos getTreePosition() {
        return pos;
    }

    public static final Simple112BlobTreeConfig FOREST_OAK_TREE = new Simple112BlobTreeConfig(tester -> {tester.nextInt(5).notEqualTo(0); tester.nextInt(10).notEqualTo(0);}, 4,2,12);
    public static final Simple112BlobTreeConfig FOREST_BIRCH_TREE = new Simple112BlobTreeConfig(tester -> {tester.nextInt(5).equalTo(0); tester.nextInt(10).notEqualTo(0);}, 5,2,12);
    public static class Simple112BlobTreeConfig {
        ITreeTypeSelector selector;

        int baseHeight;
        int firstRandomHeight;

        int mutableLeafCount;

        public Simple112BlobTreeConfig(ITreeTypeSelector selector, int baseHeight, int firstRandomHeight, int mutableLeafCount) {
            this.selector = selector;
            this.baseHeight = baseHeight;
            this.firstRandomHeight = firstRandomHeight;
            this.mutableLeafCount = mutableLeafCount;
        }
    }
}
