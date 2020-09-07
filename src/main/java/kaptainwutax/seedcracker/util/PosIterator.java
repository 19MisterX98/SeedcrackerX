package kaptainwutax.seedcracker.util;

import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class PosIterator {

    public static Set<BlockPos> create(BlockPos start, BlockPos end) {
        Set<BlockPos> result = new HashSet<>();

        for(int x = start.getX(); x <= end.getX(); x++) {
            for(int z = start.getZ(); z <= end.getZ(); z++) {
                for(int y = start.getY(); y <= end.getY(); y++) {
                    result.add(new BlockPos(x, y, z));
                }
            }
        }

        return result;
    }

}
