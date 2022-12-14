package kaptainwutax.seedcrackerX.mixin;

import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.config.StructureSave;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {


    @Shadow public abstract DynamicRegistryManager getRegistryManager();

    @Inject(method = "disconnect", at = @At("HEAD"))
    private void disconnect(CallbackInfo ci) {
        StructureSave.saveStructures(SeedCracker.get().getDataStorage().baseSeedData);
        SeedCracker.get().reset();
    }

    @Inject(method = "getGeneratorStoredBiome", at = @At("HEAD"), cancellable = true)
    private void getGeneratorStoredBiome(int x, int y, int z, CallbackInfoReturnable<RegistryEntry<Biome>> ci) {
        var biome = getRegistryManager().get(RegistryKeys.BIOME).getEntry(BiomeKeys.THE_VOID);
        biome.ifPresent(ci::setReturnValue);
    }

}
