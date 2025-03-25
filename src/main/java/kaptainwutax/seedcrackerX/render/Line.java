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
    public void render(MatrixStack.Entry matrix4f, VertexConsumer vertexConsumer, Vec3d cameraPos) {
        Vec3d normal = this.end.subtract(this.start).normalize();
        this.putVertex(vertexConsumer, matrix4f, this.start,normal, cameraPos);
        this.putVertex(vertexConsumer, matrix4f, this.end, normal, cameraPos);
    }

    protected void putVertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix4f, Vec3d pos, Vec3d normal, Vec3d cameraPos) {
        vertexConsumer.vertex(
                matrix4f,
                (float) (pos.x - cameraPos.x),
                (float) (pos.y - cameraPos.y),
                (float) (pos.z - cameraPos.z)
        ).color(
                this.color.getFRed(),
                this.color.getFGreen(),
                this.color.getFBlue(),
                1.0F
        ).normal(
                matrix4f,
                (float) normal.getX(),
                (float) normal.getY(),
                (float) normal.getZ()
        );
    }

    @Override
    public BlockPos getPos() {
        double x = (this.end.getX() - this.start.getX()) / 2 + this.start.getX();
        double y = (this.end.getY() - this.start.getY()) / 2 + this.start.getY();
        double z = (this.end.getZ() - this.start.getZ()) / 2 + this.start.getZ();
        return BlockPos.ofFloored(x, y, z);
    }

}
