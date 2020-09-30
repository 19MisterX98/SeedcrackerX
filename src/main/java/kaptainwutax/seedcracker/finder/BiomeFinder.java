package kaptainwutax.seedcracker.finder;

import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.BiomeData;
import kaptainwutax.seedcracker.cracker.DataAddedEvent;
import kaptainwutax.seedcracker.render.Color;
import kaptainwutax.seedcracker.render.Cube;
import kaptainwutax.seedcracker.util.BiomeFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class BiomeFinder extends Finder {

    public BiomeFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos);
    }

    @Override
    public List<BlockPos> findInChunk() {
        List<BlockPos> result = new ArrayList<>();

        for(int x = 0; x < 16; x += 4) {
            for(int z = 0; z < 16; z += 4) {
                BlockPos blockPos = this.chunkPos.getStartPos().add(x, 0, z);
                Biome biome = this.world.getBiomeForNoiseGen(blockPos.getX() >> 2, 0, blockPos.getZ() >> 2);

                //TODO: Fix this multi-threading issue.
                if(biome == BuiltinBiomes.THE_VOID) {
                    continue;
                }

                kaptainwutax.biomeutils.Biome otherBiome = BiomeFixer.swap(biome);

                BiomeData data = new BiomeData(otherBiome, blockPos.getX() >> 2, blockPos.getZ() >> 2);

                if(SeedCracker.get().getDataStorage().addBiomeData(data, DataAddedEvent.POKE_BIOMES)) {
                    blockPos = this.world.getTopPosition(Heightmap.Type.WORLD_SURFACE, blockPos).down();
                    result.add(blockPos);
                }
            }
        }

        result.forEach(pos -> {
            this.renderers.add(new Cube(pos, new Color(51, 204, 128)));
        });

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new BiomeFinder(world, chunkPos));
        return finders;
    }

}
