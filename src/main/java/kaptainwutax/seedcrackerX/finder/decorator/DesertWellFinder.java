package kaptainwutax.seedcrackerX.finder.decorator;

import com.seedfinding.mcfeature.decorator.DesertWell;
import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.finder.structure.PieceFinder;
import kaptainwutax.seedcrackerX.render.Color;
import kaptainwutax.seedcrackerX.render.Cube;
import kaptainwutax.seedcrackerX.render.Cuboid;
import kaptainwutax.seedcrackerX.util.BiomeFixer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class DesertWellFinder extends PieceFinder {

    protected static Vec3i SIZE = new Vec3i(5, 6, 5);

    public DesertWellFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Direction.NORTH, SIZE);
        this.searchPositions = CHUNK_POSITIONS;
        this.buildStructure();
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new DesertWellFinder(world, chunkPos));

        finders.add(new DesertWellFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z)));
        finders.add(new DesertWellFinder(world, new ChunkPos(chunkPos.x, chunkPos.z - 1)));
        finders.add(new DesertWellFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));

        finders.add(new DesertWellFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z)));
        finders.add(new DesertWellFinder(world, new ChunkPos(chunkPos.x, chunkPos.z + 1)));
        finders.add(new DesertWellFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z + 1)));

        finders.add(new DesertWellFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));
        finders.add(new DesertWellFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z + 1)));
        return finders;
    }

    @Override
    public List<BlockPos> findInChunk() {
        Biome biome = this.world.getBiomeForNoiseGen((this.chunkPos.x << 2) + 2, 0, (this.chunkPos.z << 2) + 2).value();

        if (!Features.DESERT_WELL.isValidBiome(BiomeFixer.swap(biome))) {
            return new ArrayList<>();
        }

        List<BlockPos> result = super.findInChunk();

        result.forEach(pos -> {
            pos = pos.add(2, 1, 2);

            DesertWell.Data data = Features.DESERT_WELL.at(pos.getX(), pos.getZ());

            if (SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_STRUCTURES)) {
                this.renderers.add(new Cuboid(pos.add(-2, -1, -2), SIZE, new Color(128, 128, 255)));
                this.renderers.add(new Cube(pos, new Color(128, 128, 255)));
            }
        });

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

    protected void buildStructure() {
        BlockState sandstone = Blocks.SANDSTONE.getDefaultState();
        BlockState sandstoneSlab = Blocks.SANDSTONE_SLAB.getDefaultState();
        BlockState water = Blocks.WATER.getDefaultState();

        this.fillWithOutline(0, 0, 0, 4, 1, 4, sandstone, sandstone, false);
        this.fillWithOutline(1, 5, 1, 3, 5, 3, sandstoneSlab, sandstoneSlab, false);
        this.addBlock(sandstone, 2, 5, 2);

        BlockPos p1 = new BlockPos(2, 1, 2);
        this.addBlock(water, p1.getX(), p1.getY(), p1.getZ());

        Direction.Type.HORIZONTAL.forEach(facing -> {
            BlockPos p2 = p1.offset(facing);
            this.addBlock(water, p2.getX(), p2.getY(), p2.getZ());
        });
    }

}
