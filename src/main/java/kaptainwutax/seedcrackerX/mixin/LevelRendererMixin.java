package kaptainwutax.seedcrackerX.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import kaptainwutax.seedcrackerX.render.EndMainPassEvent;
import kaptainwutax.seedcrackerX.render.ExtractStateEvent;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow @Final private LevelRenderState levelRenderState;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;extractBlockDestroyAnimation(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/state/LevelRenderState;)V", shift = At.Shift.AFTER))
    private void extractRenderStateEvent(CallbackInfo ci, @Local(argsOnly = true) Camera camera, @Local(argsOnly = true) DeltaTracker deltaTracker, @Local ProfilerFiller profiler) {
        profiler.popPush("seedcrackerxExtract");
        ExtractStateEvent.EXTRACT_STATE.invoker().extractState(this.levelRenderState, camera, deltaTracker);
    }

    @Inject(method = "lambda$addMainPass$1", at = @At(value = "INVOKE:LAST", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch()V"))
    private void callEndMainPassEvent(CallbackInfo ci, @Local MultiBufferSource.BufferSource bufferSource, @Local PoseStack poseStack, @Local(argsOnly = true) LevelRenderState renderState, @Local(argsOnly = true) ProfilerFiller profiler) {
        profiler.popPush("seedcrackerxEndMainPass");
        EndMainPassEvent.END_MAIN_PASS.invoker().endMainPass(bufferSource, poseStack, renderState);
    }
}
