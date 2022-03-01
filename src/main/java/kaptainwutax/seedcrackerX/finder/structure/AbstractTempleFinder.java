package kaptainwutax.seedcrackerX.finder.structure;

import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.render.Color;
import kaptainwutax.seedcrackerX.render.Cube;
import kaptainwutax.seedcrackerX.render.Cuboid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTempleFinder extends Finder {

    protected static List<BlockPos> SEARCH_POSITIONS;
    protected final Vec3i size;
    protected List<PieceFinder> finders = new ArrayList<>();

    public AbstractTempleFinder(World world, ChunkPos chunkPos, Vec3i size) {
        super(world, chunkPos);

        Direction.Type.HORIZONTAL.forEach(direction -> {
            PieceFinder finder = new PieceFinder(world, chunkPos, direction, size);

            finder.searchPositions = SEARCH_POSITIONS;

            buildStructure(finder);
            this.finders.add(finder);
        });

        this.size = size;
    }

    public static void reloadSearchPositions() {
        SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> {
            if (pos.getX() != 0) return true;
            if (pos.getY() < 0) return true;
            if (pos.getY() > 200) return true;
            return pos.getZ() != 0;
        });
    }

    public List<BlockPos> findInChunkPiece(PieceFinder pieceFinder) {
        Biome biome = this.world.getBiomeForNoiseGen((this.chunkPos.x << 2) + 2, 64, (this.chunkPos.z << 2) + 2).value();

        if (!isValidBiome(biome)) {
            return new ArrayList<>();
        }

        return pieceFinder.findInChunk();
    }

    protected abstract boolean isValidBiome(Biome biome);

    public void addRenderers(PieceFinder pieceFinder, BlockPos origin, Color color) {
        this.renderers.add(new Cuboid(origin, pieceFinder.getLayout(), color));
        BlockPos chunkStart = new BlockPos(origin.getX() & -16, origin.getY(), origin.getZ() & -16);
        this.renderers.add(new Cube(chunkStart, color));
    }

    public Map<PieceFinder, List<BlockPos>> findInChunkPieces() {
        Map<PieceFinder, List<BlockPos>> result = new HashMap<>();

        this.finders.forEach(pieceFinder -> {
            result.put(pieceFinder, this.findInChunkPiece(pieceFinder));
        });

        return result;
    }

    public abstract void buildStructure(PieceFinder finder);

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }
}
