package kaptainwutax.seedcrackerX.finder;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import kaptainwutax.seedcrackerX.config.Config;
import net.minecraft.client.render.*;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4f;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class FinderQueue {

    private final static FinderQueue INSTANCE = new FinderQueue();
    public static ExecutorService SERVICE = Executors.newFixedThreadPool(Config.threads);

    public FinderControl finderControl = new FinderControl();

    private FinderQueue() {
        this.clear();
    }

    public static FinderQueue get() {
        return INSTANCE;
    }

    public void onChunkData(World world, ChunkPos chunkPos) {
        if (!Config.get().active) return;

        getActiveFinderTypes().forEach(type -> {
            SERVICE.submit(() -> {
                try {
                    List<Finder> finders = type.finderBuilder.build(world, chunkPos);

                    finders.forEach(finder -> {
                        if (finder.isValidDimension(world.getDimension())) {
                            finder.findInChunk();
                            this.finderControl.addFinder(type, finder);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public void renderFinders(Matrix4f matrix4f, Camera camera) {
        if (Config.get().render == Config.RenderType.OFF) return;

        Vec3d camPos = camera.getPos();



        if (Config.get().render == Config.RenderType.XRAY) {
            RenderSystem.disableDepthTest();
        }
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.lineWidth(2.0f);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        this.finderControl.getActiveFinders().forEach(finder -> {
            if (finder.shouldRender()) {
                finder.render(matrix4f, buffer, camPos);
            }
        });

        var builtBuffer = buffer.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram(builtBuffer);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();

        RenderSystem.applyModelViewMatrix();
    }

    public List<Finder.Type> getActiveFinderTypes() {
        return Arrays.stream(Finder.Type.values())
                .filter(type -> type.enabled.get())
                .collect(Collectors.toList());
    }

    public void clear() {
        this.finderControl = new FinderControl();
    }

}
