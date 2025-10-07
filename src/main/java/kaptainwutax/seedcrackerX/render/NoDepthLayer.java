package kaptainwutax.seedcrackerX.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.OptionalDouble;

public final class NoDepthLayer {
    private NoDepthLayer() {
    }

    private static final RenderPipeline LINES_NO_DEPTH_PIPELINE = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
            .withLocation(ResourceLocation.fromNamespaceAndPath("seedcrackerx", "pipeline/lines_no_depth"))
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build()
    );
    public static final RenderType LINES_NO_DEPTH_LAYER = RenderType.create("seedcrackerx_no_depth", 3 * 512, LINES_NO_DEPTH_PIPELINE, RenderType.CompositeState.builder()
        .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
        .setLineState(new RenderType.LineStateShard(OptionalDouble.of(2)))
        .createCompositeState(false));
}
