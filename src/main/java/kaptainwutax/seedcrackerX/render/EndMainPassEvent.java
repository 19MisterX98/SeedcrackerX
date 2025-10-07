package kaptainwutax.seedcrackerX.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.LevelRenderState;

@FunctionalInterface
public interface EndMainPassEvent {
    Event<EndMainPassEvent> END_MAIN_PASS = EventFactory.createArrayBacked(EndMainPassEvent.class, listeners -> (bufferSource, poseStack, state) -> {
        for (EndMainPassEvent listener : listeners) {
            listener.endMainPass(bufferSource, poseStack, state);
        }
    });

    void endMainPass(MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, LevelRenderState state);
}
