package kaptainwutax.seedcrackerX.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Line extends Renderer {

    public Vec3 start;
    public Vec3 end;
    public Color color;

    public Line() {
        this(Vec3.ZERO, Vec3.ZERO, Color.WHITE);
    }

    public Line(Vec3 start, Vec3 end) {
        this(start, end, Color.WHITE);
    }

    public Line(Vec3 start, Vec3 end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    @Override
    public void render(Matrix4f matrix4f, VertexConsumer vertexConsumer, Vec3 cameraPos) {
        this.putVertex(vertexConsumer, matrix4f, this.start, cameraPos);
        this.putVertex(vertexConsumer, matrix4f, this.end, cameraPos);
    }

    protected void putVertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Vec3 pos, Vec3 cameraPos) {
        Vector4f vec = new Vector4f((float) (pos.x() - cameraPos.x()), (float) (pos.y() - cameraPos.y()), (float) (pos.z() - cameraPos.z()), 1.0F);
        vec.mul(matrix4f);
        vertexConsumer.addVertex(vec.x(), vec.y(), vec.z()).setColor(
                this.color.getFRed(),
                this.color.getFGreen(),
                this.color.getFBlue(),
                1.0F
        );
    }

    @Override
    public BlockPos getPos() {
        double x = (this.end.x() - this.start.x()) / 2 + this.start.x();
        double y = (this.end.y() - this.start.y()) / 2 + this.start.y();
        double z = (this.end.z() - this.start.z()) / 2 + this.start.z();
        return BlockPos.containing(x, y, z);
    }

}
