package kaptainwutax.seedcrackerX.finder;

import com.seedfinding.mcbiome.biome.Biomes;
import com.seedfinding.mccore.version.MCVersion;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.cracker.BiomeData;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.render.Color;
import kaptainwutax.seedcrackerX.render.Cube;
import kaptainwutax.seedcrackerX.util.BiomeFixer;
import kaptainwutax.seedcrackerX.util.Log;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class BiomeFinder extends Finder {

    public BiomeFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new BiomeFinder(world, chunkPos));
        return finders;
    }

    @Override
    public List<BlockPos> findInChunk() {
        List<BlockPos> result = new ArrayList<>();

        for (int x = 0; x < 16; x += 4) {
            for (int z = 0; z < 16; z += 4) {
                BlockPos blockPos = this.chunkPos.getStartPos().add(x, 0, z);
                Biome biome;
                if (Config.get().getVersion().isNewerOrEqualTo(MCVersion.v1_15)) {
                    biome = this.world.getBiomeForNoiseGen(blockPos.getX() >> 2, 0, blockPos.getZ() >> 2).value();
                } else {
                    biome = this.world.getBiome(blockPos).value();

                }
                com.seedfinding.mcbiome.biome.Biome otherBiome = BiomeFixer.swap(biome);
                if (otherBiome == Biomes.THE_VOID) {
                    continue;
                }

                BiomeData data;
                if (Config.get().getVersion().isNewerOrEqualTo(MCVersion.v1_15)) {
                    data = new BiomeData(otherBiome, blockPos.getX() >> 2, blockPos.getZ() >> 2);
                } else {
                    data = new BiomeData(otherBiome, blockPos.getX(), blockPos.getZ());
                }
                if (SeedCracker.get().getDataStorage().addBiomeData(data, DataAddedEvent.POKE_BIOMES)) {
                    blockPos = this.world.getTopPosition(Heightmap.Type.WORLD_SURFACE, blockPos).down();
                    if (Config.get().debug) Log.warn(blockPos.toShortString() + ", " + otherBiome.getName());
                    result.add(blockPos);
                }
            }
        }
        result.forEach(pos -> this.renderers.add(new Cube(pos, new Color(51, 204, 128))));

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

}
