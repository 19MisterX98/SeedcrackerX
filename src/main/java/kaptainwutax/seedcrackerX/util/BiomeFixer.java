package kaptainwutax.seedcrackerX.util;

import com.seedfinding.mcbiome.biome.Biome;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class BiomeFixer {

    private static final Map<String, Biome> COMPATREGISTRY = new HashMap<>();

    static {
        for (Biome biome : com.seedfinding.mcbiome.biome.Biomes.REGISTRY.values()) {
            COMPATREGISTRY.put(biome.getName(), biome);
        }
        //renamed
        COMPATREGISTRY.put("snowy_plains", com.seedfinding.mcbiome.biome.Biomes.SNOWY_TUNDRA);
        COMPATREGISTRY.put("old_growth_birch_forest", com.seedfinding.mcbiome.biome.Biomes.TALL_BIRCH_FOREST);
        COMPATREGISTRY.put("old_growth_pine_taiga", com.seedfinding.mcbiome.biome.Biomes.GIANT_TREE_TAIGA);
        COMPATREGISTRY.put("old_growth_spruce_taiga", com.seedfinding.mcbiome.biome.Biomes.GIANT_TREE_TAIGA);
        COMPATREGISTRY.put("windswept_hills", com.seedfinding.mcbiome.biome.Biomes.EXTREME_HILLS);
        COMPATREGISTRY.put("windswept_forest", com.seedfinding.mcbiome.biome.Biomes.WOODED_MOUNTAINS);
        COMPATREGISTRY.put("windswept_gravelly_hills", com.seedfinding.mcbiome.biome.Biomes.GRAVELLY_MOUNTAINS);
        COMPATREGISTRY.put("windswept_savanna", com.seedfinding.mcbiome.biome.Biomes.SHATTERED_SAVANNA);
        COMPATREGISTRY.put("sparse_jungle", com.seedfinding.mcbiome.biome.Biomes.JUNGLE_EDGE);
        COMPATREGISTRY.put("stony_shore", com.seedfinding.mcbiome.biome.Biomes.STONE_SHORE);
        //new
        COMPATREGISTRY.put("meadow", com.seedfinding.mcbiome.biome.Biomes.PLAINS);
        COMPATREGISTRY.put("grove", com.seedfinding.mcbiome.biome.Biomes.TAIGA);
        COMPATREGISTRY.put("snowy_slopes", com.seedfinding.mcbiome.biome.Biomes.SNOWY_TUNDRA);
        COMPATREGISTRY.put("frozen_peaks", com.seedfinding.mcbiome.biome.Biomes.TAIGA);
        COMPATREGISTRY.put("jagged_peaks", com.seedfinding.mcbiome.biome.Biomes.TAIGA);
        COMPATREGISTRY.put("stony_peaks", com.seedfinding.mcbiome.biome.Biomes.TAIGA);
        COMPATREGISTRY.put("mangrove_swamp", com.seedfinding.mcbiome.biome.Biomes.SWAMP);

        //unsure what to do with those, they'll return THE_VOID for now
        //dripstone_caves
        //lush_caves
        //deep_dark
    }

    public static Biome swap(net.minecraft.world.level.biome.Biome biome) {
        ClientPacketListener networkHandler = Minecraft.getInstance().getConnection();
        if (networkHandler == null) return com.seedfinding.mcbiome.biome.Biomes.THE_VOID;

        ResourceLocation biomeID = networkHandler.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome);

        if (biomeID == null) return com.seedfinding.mcbiome.biome.Biomes.THE_VOID;

        return COMPATREGISTRY.getOrDefault(biomeID.getPath(), com.seedfinding.mcbiome.biome.Biomes.THE_VOID);
    }

    public static net.minecraft.world.level.biome.Biome swap(Biome biome) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return null;
        var biomeRegistry = minecraft.level.registryAccess().registryOrThrow(Registries.BIOME);
        return biomeRegistry.getOptional(ResourceKey.create(Registries.BIOME, ResourceLocation.withDefaultNamespace(biome.getName())))
                .orElseGet(() -> biomeRegistry.getOrThrow(net.minecraft.world.level.biome.Biomes.THE_VOID));
    }
}
