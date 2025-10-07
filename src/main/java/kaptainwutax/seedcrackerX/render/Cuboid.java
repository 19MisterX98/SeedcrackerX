package kaptainwutax.seedcrackerX.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

public class Cuboid {
    private final AABB box;
    private final int argb;
    private final BlockPos centerPos;

    public Cuboid(AABB box, int argb) {
        this.box = box;
        this.argb = argb;
        this.centerPos = BlockPos.containing(box.getCenter());
    }

    public Cuboid(BoundingBox boundingBox, int argb) {
        this(AABB.of(boundingBox), argb);
    }

    public Cuboid(BlockPos pos, int argb) {
        this(new AABB(pos), argb);
    }

    public Cuboid(BlockPos pos, Vec3i size, int argb) {
        this(AABB.encapsulatingFullBlocks(pos, pos.offset(size)), argb);
    }

    public BlockPos getCenterPos() {
        return this.centerPos;
    }

    public Cuboid offset(Camera camera) {
        return new Cuboid(this.box.move(camera.position().scale(-1)), this.argb);
    }

    public void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource) {
        float alpha = ARGB.alphaFloat(this.argb);
        float red = ARGB.redFloat(this.argb);
        float green = ARGB.greenFloat(this.argb);
        float blue = ARGB.blueFloat(this.argb);
        ShapeRenderer.renderLineBox(poseStack.last(), bufferSource.getBuffer(NoDepthLayer.LINES_NO_DEPTH_LAYER), this.box, red, green, blue, alpha);
    }
}
