package kaptainwutax.seedcrackerX.render;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

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
    public void render(Matrix4f matrix4f, VertexConsumer vertexConsumer, Vec3d cameraPos) {
        this.putVertex(vertexConsumer, matrix4f, this.start, cameraPos);
        this.putVertex(vertexConsumer, matrix4f, this.end, cameraPos);
    }

    protected void putVertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Vec3d pos, Vec3d cameraPos) {
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
