package kaptainwutax.seedcrackerX.finder.structure;

import com.seedfinding.mcfeature.structure.RegionStructure;
import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.util.BiomeFixer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StairsShape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SwampHutFinder extends AbstractTempleFinder {

    public SwampHutFinder(Level world, ChunkPos chunkPos) {
        super(world, chunkPos, new Vec3i(7, 7, 9));
    }

    public static List<Finder> create(Level world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new SwampHutFinder(world, chunkPos));
        return finders;
    }

    @Override
    public List<BlockPos> findInChunk() {
        Map<PieceFinder, List<BlockPos>> result = super.findInChunkPieces();
        List<BlockPos> combinedResult = new ArrayList<>();

        result.forEach((pieceFinder, positions) -> {
            combinedResult.addAll(positions);

            positions.forEach(pos -> {
                RegionStructure.Data<?> data = Features.SWAMP_HUT.at(this.chunkPos.x, this.chunkPos.z);

                if (SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_LIFTING)) {
                    this.addRenderers(pieceFinder, pos, ARGB.color(255, 0, 255));
                }
            });
        });

        return combinedResult;
    }

    @Override
    protected boolean isValidBiome(Biome biome) {
        return Features.SWAMP_HUT.isValidBiome(BiomeFixer.swap(biome));
    }

    @Override
    public void buildStructure(PieceFinder finder) {
        finder.fillWithOutline(1, 1, 1, 5, 1, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        finder.fillWithOutline(1, 4, 2, 5, 4, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        finder.fillWithOutline(2, 1, 0, 4, 1, 0, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        finder.fillWithOutline(2, 2, 2, 3, 3, 2, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        finder.fillWithOutline(1, 2, 3, 1, 3, 6, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        finder.fillWithOutline(5, 2, 3, 5, 3, 6, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        finder.fillWithOutline(2, 2, 7, 4, 3, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        finder.fillWithOutline(1, 0, 2, 1, 3, 2, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
        finder.fillWithOutline(5, 0, 2, 5, 3, 2, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
        finder.fillWithOutline(1, 0, 7, 1, 3, 7, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
        finder.fillWithOutline(5, 0, 7, 5, 3, 7, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
        finder.addBlock(Blocks.OAK_FENCE.defaultBlockState(), 2, 3, 2);
        finder.addBlock(Blocks.OAK_FENCE.defaultBlockState(), 3, 3, 7);
        finder.addBlock(Blocks.AIR.defaultBlockState(), 1, 3, 4);
        finder.addBlock(Blocks.AIR.defaultBlockState(), 5, 3, 4);
        finder.addBlock(Blocks.AIR.defaultBlockState(), 5, 3, 5);
        finder.addBlock(Blocks.POTTED_RED_MUSHROOM.defaultBlockState(), 1, 3, 5);
        finder.addBlock(Blocks.CRAFTING_TABLE.defaultBlockState(), 3, 2, 6);
        finder.addBlock(Blocks.CAULDRON.defaultBlockState(), 4, 2, 6);
        finder.addBlock(Blocks.OAK_FENCE.defaultBlockState(), 1, 2, 1);
        finder.addBlock(Blocks.OAK_FENCE.defaultBlockState(), 5, 2, 1);
        BlockState northStairs = Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH);
        BlockState eastStairs = Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.EAST);
        BlockState westStairs = Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.WEST);
        BlockState southStairs = Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH);
        finder.fillWithOutline(0, 4, 1, 6, 4, 1, northStairs, northStairs, false);
        finder.fillWithOutline(0, 4, 2, 0, 4, 7, eastStairs, eastStairs, false);
        finder.fillWithOutline(6, 4, 2, 6, 4, 7, westStairs, westStairs, false);
        finder.fillWithOutline(0, 4, 8, 6, 4, 8, southStairs, southStairs, false);
        finder.addBlock(northStairs.setValue(StairBlock.SHAPE, StairsShape.OUTER_RIGHT), 0, 4, 1);
        finder.addBlock(northStairs.setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT), 6, 4, 1);
        finder.addBlock(southStairs.setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT), 0, 4, 8);
        finder.addBlock(southStairs.setValue(StairBlock.SHAPE, StairsShape.OUTER_RIGHT), 6, 4, 8);
    }

}
