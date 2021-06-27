package kaptainwutax.seedcrackerX.cracker.decorator.decoratorData;

import me.cortex.TreeCracker.trees.Simple116BlobTree;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class TreeData {

    public BlockPos pos;
    public List<Integer> leaves = new ArrayList<>();
    public int height;
    public Simple116BlobTree.Simple116BlobTreeConfig type;
    public String name;

    public void setType(Block log) {
        if (log.equals(Blocks.OAK_LOG)) {
            type = Simple116BlobTree.FOREST_OAK_TREE;
            name = "FOREST_OAK_TREE";
        } else {
            type = Simple116BlobTree.FOREST_BIRCH_TREE;
            name = "FOREST_BIRCH_TREE";
        }
    }

    public String leavesToString() {
        if (leaves.isEmpty())
            return null;
        StringBuilder result = new StringBuilder();
        for (Integer leave : this.leaves) {
            result.append(leave);
            if(result.length() < 23) {
                result.append(",");
            }
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return "new Simple116BlobTree(Simple116BlobTree." + name + ", new TreePos(" + (pos.getX() & 15) +
                ", " + (pos.getZ() & 15) + "), " + height + ", new int[]{" + leavesToString() + "}),";
    }

    public int[] leavesToArray() {
        int[] result = new int[12];
        for(int i = 0; i < 12; i++) {
            result[i] = leaves.get(i);
        }
        return result;
    }
}