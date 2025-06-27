package kaptainwutax.seedcrackerX.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
public interface EndMainPassEvent {
    Event<EndMainPassEvent> END_MAIN_PASS = EventFactory.createArrayBacked(EndMainPassEvent.class, callbacks -> context -> {
        for (EndMainPassEvent callback : callbacks) {
            callback.endMainPass(context);
        }
    });

    void endMainPass(WorldRenderContext context);
}
