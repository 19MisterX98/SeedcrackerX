package kaptainwutax.seedcrackerX.render;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

public class Cube extends Cuboid {

    public Cube() {
        this(BlockPos.ZERO, Color.WHITE);
    }

    public Cube(BlockPos pos) {
        this(pos, Color.WHITE);
    }

    public Cube(BlockPos pos, Color color) {
        super(pos, new Vec3i(1, 1, 1), color);
    }

    @Override
    public BlockPos getPos() {
        return this.start;
    }

}
