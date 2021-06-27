package me.cortex.TreeCracker.trees;

public class TreePos {
    public final int x;
    public final int z;

    public TreePos(int x, int z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public String toString() {
        return "TreePos{" +
                "x=" + x +
                ", z=" + z +
                '}';
    }
}
