package kaptainwutax.seedcrackerX.finder.structure;

import com.seedfinding.mcfeature.structure.RegionStructure;
import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.finder.Finder;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonumentFinder extends Finder {

    protected static List<BlockPos> SEARCH_POSITIONS;
    protected final Vec3i size = new Vec3i(8, 5, 8);
    protected List<PieceFinder> finders = new ArrayList<>();

    public MonumentFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos);

        PieceFinder finder = new PieceFinder(world, chunkPos, Direction.NORTH, size);

        finder.searchPositions = SEARCH_POSITIONS;

        buildStructure(finder);
        this.finders.add(finder);
    }

    public static void reloadSearchPositions() {
        SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> pos.getY() != 56);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new MonumentFinder(world, chunkPos));
        finders.add(new MonumentFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z)));
        finders.add(new MonumentFinder(world, new ChunkPos(chunkPos.x, chunkPos.z - 1)));
        finders.add(new MonumentFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));
        return finders;
    }

    @Override
    public List<BlockPos> findInChunk() {
        Biome biome = this.world.getBiomeForNoiseGen((this.chunkPos.x << 2) + 2, 64, (this.chunkPos.z << 2) + 2).value();
        if (BiomeFixer.swap(biome).getCategory() != com.seedfinding.mcbiome.biome.Biome.Category.OCEAN) return new ArrayList<>();
        Map<PieceFinder, List<BlockPos>> result = this.findInChunkPieces();
        List<BlockPos> combinedResult = new ArrayList<>();

        result.forEach((pieceFinder, positions) -> {
            positions.removeIf(pos -> {
                //Figure this out, it's not a trivial task.
                return false;
            });

            combinedResult.addAll(positions);

            positions.forEach(pos -> {
                ChunkPos monumentStart = new ChunkPos(this.chunkPos.x + 1, this.chunkPos.z + 1);
                RegionStructure.Data<?> data = Features.MONUMENT.at(monumentStart.x, monumentStart.z);

                if (SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_STRUCTURES)) {
                    this.renderers.add(new Cuboid(pos, pieceFinder.getLayout(), new Color(0, 0, 255)));
                    this.renderers.add(new Cube(monumentStart.getStartPos().add(0, pos.getY(), 0), new Color(0, 0, 255)));
                }
            });
        });

        return combinedResult;
    }

    public Map<PieceFinder, List<BlockPos>> findInChunkPieces() {
        Map<PieceFinder, List<BlockPos>> result = new HashMap<>();

        this.finders.forEach(pieceFinder -> {
            result.put(pieceFinder, pieceFinder.findInChunk());
        });

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

    public void buildStructure(PieceFinder finder) {
        BlockState prismarine = Blocks.PRISMARINE.getDefaultState();
        BlockState prismarineBricks = Blocks.PRISMARINE_BRICKS.getDefaultState();
        BlockState darkPrismarine = Blocks.DARK_PRISMARINE.getDefaultState();
        BlockState seaLantern = Blocks.SEA_LANTERN.getDefaultState();
        BlockState water = Blocks.WATER.getDefaultState();

        //4 bottom pillars.
        for (int i = 0; i < 4; i++) {
            int x = i >= 2 ? 7 : 0;
            int z = i % 2 == 0 ? 0 : 7;

            for (int y = 0; y < 3; y++) {
                finder.addBlock(prismarineBricks, x, y, z);
            }
        }

        //First bend.
        for (int i = 0; i < 4; i++) {
            int x = i >= 2 ? 6 : 1;
            int z = i % 2 == 0 ? 1 : 6;
            finder.addBlock(prismarineBricks, x, 3, z);
        }

        //Prismarine ring.
        for (int x = 2; x <= 5; x++) {
            for (int z = 2; z <= 5; z++) {
                if (x == 2 || x == 5 || z == 2 || z == 5) {
                    finder.addBlock(prismarine, x, 4, z);
                }
            }
        }

        //Second bend.
        for (int i = 0; i < 4; i++) {
            int x = i >= 2 ? 5 : 2;
            int z = i % 2 == 0 ? 2 : 5;
            finder.addBlock(prismarineBricks, x, 4, z);
            finder.addBlock(seaLantern, x, 3, z);
        }
    }

}
