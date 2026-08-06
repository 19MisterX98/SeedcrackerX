package kaptainwutax.seedcrackerX.finder.decorator.ore;

import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.cracker.decorator.EmeraldOre;
import kaptainwutax.seedcrackerX.finder.BlockFinder;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.render.Color;
import kaptainwutax.seedcrackerX.render.Cube;
import kaptainwutax.seedcrackerX.util.BiomeFixer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class EmeraldOreFinder extends BlockFinder {

    protected static List<BlockPos> SEARCH_POSITIONS;

    public EmeraldOreFinder(Level world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.EMERALD_ORE);
        this.searchPositions = SEARCH_POSITIONS;
    }

    public static void reloadSearchPositions() {
        SEARCH_POSITIONS = Finder.buildSearchPositions(Finder.CHUNK_POSITIONS, pos -> {
            if (pos.getY() < 4) return true;
            return pos.getY() > 28 + 4;
        });
    }

    public static List<Finder> create(Level world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new EmeraldOreFinder(world, chunkPos));
        return finders;
    }

    @Override
    public List<BlockPos> findInChunk() {
        Biome biome = this.world.getNoiseBiome((this.chunkPos.x << 2) + 2, 0, (this.chunkPos.z << 2) + 2).value();

        List<BlockPos> result = super.findInChunk();
        if (result.isEmpty()) return result;

        BlockPos pos = result.get(0);

        EmeraldOre.Data data = Features.EMERALD_ORE.at(pos.getX(), pos.getY(), pos.getZ(), BiomeFixer.swap(biome));

        if (SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_STRUCTURES)) {
            this.renderers.add(new Cube(pos, new Color(0, 255, 0)));
        }

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

}
