package me.cortex.TreeCracker.trees;

import me.cortex.TreeCracker.LCG.LcgTester;

public interface ICrackableTree {
    TreePos getTreePosition();
    void generateTreeTest(LcgTester treeTest);
}
