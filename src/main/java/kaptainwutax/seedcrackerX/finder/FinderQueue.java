package kaptainwutax.seedcrackerX.finder;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.profile.FinderConfig;
import kaptainwutax.seedcrackerX.profile.config.ConfigScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FinderQueue {

    private final static FinderQueue INSTANCE = new FinderQueue();
    public static ExecutorService SERVICE = Executors.newFixedThreadPool(5);

    public RenderType renderType = ConfigScreen.getConfig().getRENDER();
    public FinderConfig finderProfile = new FinderConfig();

    private FinderQueue() {
        this.clear();
    }

    public static FinderQueue get() {
        return INSTANCE;
    }

    public void onChunkData(World world, ChunkPos chunkPos) {
        if(!SeedCracker.get().isActive())return;

        this.finderProfile.getActiveFinderTypes().forEach(type -> {
            SERVICE.submit(() -> {
               try {
                   List<Finder> finders = type.finderBuilder.build(world, chunkPos);

                   finders.forEach(finder -> {
                       if(finder.isValidDimension(world.getDimension())) {
                           finder.findInChunk();
                           this.finderProfile.addFinder(type, finder);
                       }
                   });
               } catch(Exception e) {
                   e.printStackTrace();
               }
            });
        });
    }

    public void renderFinders(MatrixStack matrixStack, Camera camera) {
        if(this.renderType == RenderType.OFF)return;

        matrixStack.push();

        Vec3d camPos = camera.getPos();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        if(this.renderType == RenderType.XRAY) {
            RenderSystem.disableDepthTest();
        }
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.lineWidth(2.0f);

        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        this.finderProfile.getActiveFinders().forEach(finder -> {
            if(finder.shouldRender()) {
                finder.render(matrixStack, buffer, camPos);
            }
        });

        if(buffer.isBuilding()) {
            tessellator.draw();
        }
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();

        matrixStack.pop();
    }

    public void clear() {
        this.renderType = ConfigScreen.getConfig().getRENDER();
        this.finderProfile = new FinderConfig();
    }

    public enum RenderType {
        OFF, ON, XRAY
    }

}
