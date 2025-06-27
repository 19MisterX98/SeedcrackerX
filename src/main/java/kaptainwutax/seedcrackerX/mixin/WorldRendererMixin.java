package kaptainwutax.seedcrackerX.mixin;

import kaptainwutax.seedcrackerX.render.EndMainPassEvent;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "method_62214", at = @At(value = "INVOKE:LAST", target = "Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;draw()V"))
    private void onEndMainPass(CallbackInfo ci) {
        EndMainPassEvent.END_MAIN_PASS.invoker().endMainPass(WorldRenderContext.getInstance(WorldRenderer.class.cast(this)));
    }
}
