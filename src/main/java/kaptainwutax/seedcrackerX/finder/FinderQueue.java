package kaptainwutax.seedcrackerX.finder;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.render.EndMainPassEvent;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class FinderQueue {

    private final static FinderQueue INSTANCE = new FinderQueue();
    private static final Logger log = LoggerFactory.getLogger(FinderQueue.class);
    public static ExecutorService SERVICE = Executors.newFixedThreadPool(5);

    private static final RenderPipeline LINES_NO_DEPTH_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.RENDERTYPE_LINES_SNIPPET)
                    .withLocation(Identifier.of("seedcrackerx", "pipeline/lines_no_depth"))
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .withFragmentShader(Identifier.of("seedcrackerx", "core/rendertype_lines"))
                    .build()
    );
    public static final RenderLayer LINES_NO_DEPTH_LAYER = RenderLayer.of("seedcrackerx_no_depth", 3 * 512, LINES_NO_DEPTH_PIPELINE, RenderLayer.MultiPhaseParameters.builder()
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)

            .lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(2f)))
            .build(false));

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

    static {
        EndMainPassEvent.END_MAIN_PASS.register(context -> {
            context.matrixStack().push();
            FinderQueue.get().renderFinders(context.matrixStack().peek(), context.consumers().getBuffer(FinderQueue.LINES_NO_DEPTH_LAYER), context.camera());
            context.matrixStack().pop();
        });
    }

    public void renderFinders(MatrixStack.Entry matrix4f, VertexConsumer buffer, Camera camera) {
        if (Config.get().render == Config.RenderType.OFF) return;

        Vec3d camPos = camera.getPos();

        this.finderControl.getActiveFinders().forEach(finder -> {
            if (finder.shouldRender()) {
                finder.render(matrix4f, buffer, camPos);
            }
        });
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
