package kaptainwutax.seedcrackerX.render;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public abstract class Renderer {

    public abstract void render(Matrix4f matrix, VertexConsumer vertexConsumer, Vec3d cameraPos);

    public abstract BlockPos getPos();

    protected Vec3d toVec3d(BlockPos pos) {
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

}
