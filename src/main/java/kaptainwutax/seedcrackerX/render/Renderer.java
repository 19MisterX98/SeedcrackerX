package kaptainwutax.seedcrackerX.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

// Tessellator adapter helper
public abstract class Renderer {

    public abstract void render(Matrix4f matrix, VertexConsumer vertexConsumer, Vec3 cameraPos);

    public void render(PoseStack poseStack, VertexConsumer vertexConsumer, Vec3 cameraPos) {
        render(poseStack.last().pose(), vertexConsumer, cameraPos);
    }

    public void renderWithTessellator(PoseStack poseStack, Vec3 cameraPos) {
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        render(poseStack, bufferBuilder, cameraPos);
    }

    public abstract BlockPos getPos();

    protected Vec3 toVec3d(BlockPos pos) {
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

}
