package kaptainwutax.seedcracker.finder.decorator.ore;

import kaptainwutax.seedcracker.Features;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.DataAddedEvent;
import kaptainwutax.seedcracker.cracker.decorator.EmeraldOre;
import kaptainwutax.seedcracker.finder.BlockFinder;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Color;
import kaptainwutax.seedcracker.render.Cube;
import kaptainwutax.seedcracker.util.BiomeFixer;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class EmeraldOreFinder extends BlockFinder {

    protected static List<BlockPos> SEARCH_POSITIONS = Finder.buildSearchPositions(Finder.CHUNK_POSITIONS, pos -> {
        if(pos.getY() < 4)return true;
        if(pos.getY() > 28 + 4)return true;
        return false;
    });

    public EmeraldOreFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.EMERALD_ORE);
        this.searchPositions = SEARCH_POSITIONS;
    }

    @Override
    public List<BlockPos> findInChunk() {
        Biome biome = this.world.getBiomeForNoiseGen((this.chunkPos.x << 2) + 2, 0, (this.chunkPos.z << 2) + 2);

        List<BlockPos> result = super.findInChunk();
        if(result.isEmpty())return result;

        BlockPos pos = result.get(0);

        EmeraldOre.Data data = Features.EMERALD_ORE.at(pos.getX(), pos.getY(), pos.getZ(), BiomeFixer.swap(biome));

        if(SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_STRUCTURES)) {
            this.renderers.add(new Cube(pos, new Color(0, 255, 0)));
        }

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new EmeraldOreFinder(world, chunkPos));
        return finders;
    }

}
