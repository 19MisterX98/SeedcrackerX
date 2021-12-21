package kaptainwutax.seedcrackerX.finder.structure;

import com.seedfinding.mcfeature.structure.RegionStructure;
import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.render.Color;
import kaptainwutax.seedcrackerX.render.Cube;
import kaptainwutax.seedcrackerX.render.Cuboid;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IglooFinder extends Finder {

    protected static Map<Direction, List<BlockPos>> SEARCH_POSITIONS;
    protected static final Vec3i size = new Vec3i(7, 5, 8);
    protected List<JigsawFinder> finders = new ArrayList<>();

    public IglooFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos);

        Direction.Type.HORIZONTAL.forEach(direction -> {
            JigsawFinder finder = new JigsawFinder(world, chunkPos, direction, size);

            finder.searchPositions = SEARCH_POSITIONS.get(direction);
            //finder.setDebug();
            buildStructure(finder);
            this.finders.add(finder);
        });
    }

    public static void reloadSearchPositions() {
        SEARCH_POSITIONS = JigsawFinder.getSearchPositions(3, 5,0,0, size);
    }


    @Override
    public List<BlockPos> findInChunk() {
        Map<JigsawFinder, List<BlockPos>> result = this.findInChunkPieces();
        List<BlockPos> combinedResult = new ArrayList<>();

        result.forEach((pieceFinder, positions) -> {

            combinedResult.addAll(positions);

            positions.forEach(pos -> {
                RegionStructure.Data<?> data = Features.IGLOO.at(this.chunkPos.x, this.chunkPos.z);

                if (SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_LIFTING)) {
                    this.renderers.add(new Cuboid(pos, pieceFinder.getLayout(), new Color(176, 207, 252)));
                    this.renderers.add(new Cube(chunkPos.getStartPos().add(0, pos.getY(), 0), new Color(176, 207, 252)));
                }
            });
        });

        return combinedResult;
    }

    public Map<JigsawFinder, List<BlockPos>> findInChunkPieces() {
        Map<JigsawFinder, List<BlockPos>> result = new HashMap<>();

        this.finders.forEach(pieceFinder -> {
            result.put(pieceFinder, pieceFinder.findInChunk());
        });

        return result;
    }

    public void buildStructure(JigsawFinder finder) {
        BlockState snow = Blocks.SNOW_BLOCK.getDefaultState();
        BlockState ice = Blocks.ICE.getDefaultState();
        BlockState workBench = Blocks.CRAFTING_TABLE.getDefaultState();

        finder.addBlock(workBench, 1, 1, 5);
        for(int y = 0; y < 3; y++) {
            finder.addBlock(snow, 2, y, 0);
            finder.addBlock(snow, 2, y, 1);
            finder.addBlock(snow, 1, y, 2);
            finder.addBlock(snow, 0, y, 3);
            finder.addBlock(snow, 0, y, 4);
            finder.addBlock(ice, 0, 1, 4);
            finder.addBlock(snow, 0, y, 5);
            finder.addBlock(snow, 1, y, 6);
            finder.addBlock(snow, 2, y, 7);

            finder.addBlock(snow, 3, y, 7);

            finder.addBlock(snow, 4, y, 0);
            finder.addBlock(snow, 4, y, 1);
            finder.addBlock(snow, 5, y, 2);
            finder.addBlock(snow, 6, y, 3);
            finder.addBlock(snow, 6, y, 4);
            finder.addBlock(ice, 6, 1, 4);
            finder.addBlock(snow, 6, y, 5);
            finder.addBlock(snow, 5, y, 6);
            finder.addBlock(snow, 4, y, 7);
        }
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new IglooFinder(world, chunkPos));
        finders.add(new IglooFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z)));
        return finders;
    }

}
