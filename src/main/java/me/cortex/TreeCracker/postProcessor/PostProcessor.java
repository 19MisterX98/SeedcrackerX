package me.cortex.TreeCracker.postProcessor;

import me.cortex.TreeCracker.NotImplementedException;
import me.cortex.TreeCracker.program.TreeCrackerProgram;

//TODO: CLEAN UP THIS CODE
public class PostProcessor {
    TreeCrackerProgram.Tree[] TREES;
    int LOOKBACK;

    public PostProcessor(TreeCrackerProgram.Tree[] trees, int lookback_range) {
        this.TREES = trees;
        this.LOOKBACK = lookback_range;
    }

    public boolean canTreeSeedExist(long seed) {
        throw new NotImplementedException();
    }
}
