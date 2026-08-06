package kaptainwutax.seedcrackerX.mixin;

import kaptainwutax.seedcrackerX.config.Config;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {

    @Inject(method = "getUncachedNoiseBiome", at = @At("HEAD"), cancellable = true)
    private void getUncachedNoiseBiome(int x, int y, int z, CallbackInfoReturnable<Holder<Biome>> ci) {
        if (Config.get().active) {
            ClientLevel level = (ClientLevel) (Object) this;
            var biome = level.registryAccess().registryOrThrow(Registries.BIOME).getHolder(Biomes.THE_VOID);
            biome.ifPresent(ci::setReturnValue);
        }
    }
}
