package kaptainwutax.seedcracker.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;

public class BiomeFixer {

	public static kaptainwutax.biomeutils.Biome swap(net.minecraft.world.biome.Biome biome) {
		return kaptainwutax.biomeutils.Biome.REGISTRY.get(MinecraftClient.getInstance().getNetworkHandler()
				.getRegistryManager().get(Registry.BIOME_KEY).getRawId(biome));
	}

	public static net.minecraft.world.biome.Biome swap(kaptainwutax.biomeutils.Biome biome) {
		return BuiltinRegistries.BIOME.get(biome.getId());
	}

}
