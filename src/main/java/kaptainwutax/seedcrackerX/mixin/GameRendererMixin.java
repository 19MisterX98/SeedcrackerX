package kaptainwutax.seedcrackerX.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import kaptainwutax.seedcrackerX.finder.FinderQueue;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "renderWorld", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = {"ldc=hand"}))
    private void renderWorldHand(RenderTickCounter renderTickCounter, CallbackInfo ci, @Local Camera camera, @Local(ordinal = 2) Matrix4f matrix4f3) {
        FinderQueue.get().renderFinders(matrix4f3, camera);
    }

}
