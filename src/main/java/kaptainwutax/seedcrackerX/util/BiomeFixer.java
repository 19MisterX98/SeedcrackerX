package kaptainwutax.seedcrackerX.util;

import kaptainwutax.biomeutils.biome.Biome;
import kaptainwutax.biomeutils.biome.Biomes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;

public class BiomeFixer {

	public static Biome swap(net.minecraft.world.biome.Biome biome) {
		return Biomes.REGISTRY.get(MinecraftClient.getInstance().getNetworkHandler()
				.getRegistryManager().get(Registry.BIOME_KEY).getRawId(biome));
	}

	public static net.minecraft.world.biome.Biome swap(Biome biome) {
		return BuiltinRegistries.BIOME.get(biome.getId());
	}

}
