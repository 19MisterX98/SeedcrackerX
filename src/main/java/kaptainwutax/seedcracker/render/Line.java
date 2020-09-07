package kaptainwutax.seedcracker.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
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
    public void render() {
        if(this.start == null || this.end == null || this.color == null)return;

        Vec3d camPos = this.mc.gameRenderer.getCamera().getPos();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        //This is how thick the line is.
        GlStateManager.lineWidth(2.0f);
        buffer.begin(3, VertexFormats.POSITION_COLOR);

        //Put the start and end vertices in the buffer.
        this.putVertex(buffer, camPos, this.start);
        this.putVertex(buffer, camPos, this.end);

        //Draw it all.
        tessellator.draw();
    }

    protected void putVertex(BufferBuilder buffer, Vec3d camPos, Vec3d pos) {
        buffer.vertex(
                pos.getX() - camPos.x,
                pos.getY() - camPos.y,
                pos.getZ() - camPos.z
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
