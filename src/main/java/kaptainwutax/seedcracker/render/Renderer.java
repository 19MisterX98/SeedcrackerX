package kaptainwutax.seedcracker.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class Renderer {

    protected MinecraftClient mc = MinecraftClient.getInstance();

    public abstract void render();

    public abstract BlockPos getPos();

    protected Vec3d toVec3d(BlockPos pos) {
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

}
