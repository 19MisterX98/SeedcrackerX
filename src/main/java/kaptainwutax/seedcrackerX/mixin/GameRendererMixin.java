package kaptainwutax.seedcrackerX.mixin;

import kaptainwutax.seedcrackerX.finder.FinderQueue;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private Camera camera;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = {"ldc=hand"}), locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderWorldHand(RenderTickCounter tickCounter, CallbackInfo ci, float f, boolean bl, Camera camera, Entity entity, float g, double d, Matrix4f matrix4f, MatrixStack matrixStack, float h, float i, Quaternionf quaternionf, Matrix4f matrix4f2) {
        FinderQueue.get().renderFinders(matrix4f2, camera);
    }

}
