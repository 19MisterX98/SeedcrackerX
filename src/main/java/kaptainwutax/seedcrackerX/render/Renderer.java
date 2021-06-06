package kaptainwutax.seedcrackerX.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class Renderer {

    protected MinecraftClient mc = MinecraftClient.getInstance();

    public abstract void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, Vec3d cameraPos);

    public abstract BlockPos getPos();

    protected Vec3d toVec3d(BlockPos pos) {
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

}
