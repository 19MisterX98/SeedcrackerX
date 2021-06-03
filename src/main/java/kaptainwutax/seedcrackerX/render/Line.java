package kaptainwutax.seedcrackerX.render;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Line extends Renderer {

    public Vec3d start;
    public Vec3d end;
    public Color color;

    public Line() {
        this(Vec3d.ZERO, Vec3d.ZERO, Color.WHITE);
    }

    public Line(Vec3d start, Vec3d end) {
        this(start, end, Color.WHITE);
    }

    public Line(Vec3d start, Vec3d end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer) {
        this.putVertex(vertexConsumer, matrixStack, this.start);
        this.putVertex(vertexConsumer, matrixStack, this.end);
    }

    protected void putVertex(VertexConsumer vertexConsumer, MatrixStack matrixStack, Vec3d pos) {
        vertexConsumer.vertex(
                matrixStack.peek().getModel(),
                (float)(pos.getX()),
                (float)(pos.getY()),
                (float)(pos.getZ())
        ).color(
                this.color.getFRed(),
                this.color.getFGreen(),
                this.color.getFBlue(),
                1.0F
        ).next();
    }

    @Override
    public BlockPos getPos() {
        double x = (this.end.getX() - this.start.getX()) / 2 + this.start.getX();
        double y = (this.end.getY() - this.start.getY()) / 2 + this.start.getY();
        double z = (this.end.getZ() - this.start.getZ()) / 2 + this.start.getZ();
        return new BlockPos(x, y, z);
    }

}
