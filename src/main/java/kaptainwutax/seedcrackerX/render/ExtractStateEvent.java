package kaptainwutax.seedcrackerX.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.state.LevelRenderState;

@FunctionalInterface
public interface ExtractStateEvent {
    Event<ExtractStateEvent> EXTRACT_STATE = EventFactory.createArrayBacked(ExtractStateEvent.class, listeners -> (state, camera, deltaTracker) -> {
        for (ExtractStateEvent listener : listeners) {
            listener.extractState(state, camera, deltaTracker);
        }
    });

    void extractState(LevelRenderState state, Camera camera, DeltaTracker deltaTracker);
}
