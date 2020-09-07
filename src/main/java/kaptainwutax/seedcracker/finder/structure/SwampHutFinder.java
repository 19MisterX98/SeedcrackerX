package kaptainwutax.seedcracker.finder.structure;

import kaptainwutax.featureutils.structure.RegionStructure;
import kaptainwutax.seedcracker.Features;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.DataAddedEvent;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Color;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.StairShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SwampHutFinder extends AbstractTempleFinder {

    public SwampHutFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, new Vec3i(7, 7, 9));
    }

    @Override
    public List<BlockPos> findInChunk() {
        Map<PieceFinder, List<BlockPos>> result = super.findInChunkPieces();
        List<BlockPos> combinedResult = new ArrayList<>();

        result.forEach((pieceFinder, positions) -> {
            combinedResult.addAll(positions);

            positions.forEach(pos -> {
                RegionStructure.Data<?> data = Features.SWAMP_HUT.at(this.chunkPos.x, this.chunkPos.z);

                if(SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_STRUCTURES)) {
                    this.addRenderers(pieceFinder, pos, new Color(255, 0, 255));
                }
            });
        });

        return combinedResult;
    }

    @Override
    protected StructureFeature<?> getStructureFeature() {
        return StructureFeature.SWAMP_HUT;
    }

    @Override
    public void buildStructure(PieceFinder finder) {
        finder.fillWithOutline(1, 1, 1, 5, 1, 7, Blocks.SPRUCE_PLANKS.getDefaultState(), Blocks.SPRUCE_PLANKS.getDefaultState(), false);
        finder.fillWithOutline(1, 4, 2, 5, 4, 7, Blocks.SPRUCE_PLANKS.getDefaultState(), Blocks.SPRUCE_PLANKS.getDefaultState(), false);
        finder.fillWithOutline(2, 1, 0, 4, 1, 0, Blocks.SPRUCE_PLANKS.getDefaultState(), Blocks.SPRUCE_PLANKS.getDefaultState(), false);
        finder.fillWithOutline(2, 2, 2, 3, 3, 2, Blocks.SPRUCE_PLANKS.getDefaultState(), Blocks.SPRUCE_PLANKS.getDefaultState(), false);
        finder.fillWithOutline(1, 2, 3, 1, 3, 6, Blocks.SPRUCE_PLANKS.getDefaultState(), Blocks.SPRUCE_PLANKS.getDefaultState(), false);
        finder.fillWithOutline(5, 2, 3, 5, 3, 6, Blocks.SPRUCE_PLANKS.getDefaultState(), Blocks.SPRUCE_PLANKS.getDefaultState(), false);
        finder.fillWithOutline(2, 2, 7, 4, 3, 7, Blocks.SPRUCE_PLANKS.getDefaultState(), Blocks.SPRUCE_PLANKS.getDefaultState(), false);
        finder.fillWithOutline(1, 0, 2, 1, 3, 2, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        finder.fillWithOutline(5, 0, 2, 5, 3, 2, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        finder.fillWithOutline(1, 0, 7, 1, 3, 7, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        finder.fillWithOutline(5, 0, 7, 5, 3, 7, Blocks.OAK_LOG.getDefaultState(), Blocks.OAK_LOG.getDefaultState(), false);
        finder.addBlock(Blocks.OAK_FENCE.getDefaultState(), 2, 3, 2);
        finder.addBlock(Blocks.OAK_FENCE.getDefaultState(), 3, 3, 7);
        finder.addBlock(Blocks.AIR.getDefaultState(), 1, 3, 4);
        finder.addBlock(Blocks.AIR.getDefaultState(), 5, 3, 4);
        finder.addBlock(Blocks.AIR.getDefaultState(), 5, 3, 5);
        finder.addBlock(Blocks.POTTED_RED_MUSHROOM.getDefaultState(), 1, 3, 5);
        finder.addBlock(Blocks.CRAFTING_TABLE.getDefaultState(), 3, 2, 6);
        finder.addBlock(Blocks.CAULDRON.getDefaultState(), 4, 2, 6);
        finder.addBlock(Blocks.OAK_FENCE.getDefaultState(), 1, 2, 1);
        finder.addBlock(Blocks.OAK_FENCE.getDefaultState(), 5, 2, 1);
        BlockState northStairs = Blocks.SPRUCE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH);
        BlockState eastStairs = Blocks.SPRUCE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST);
        BlockState westStairs = Blocks.SPRUCE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST);
        BlockState southStairs = Blocks.SPRUCE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH);
        finder.fillWithOutline(0, 4, 1, 6, 4, 1, northStairs, northStairs, false);
        finder.fillWithOutline(0, 4, 2, 0, 4, 7, eastStairs, eastStairs, false);
        finder.fillWithOutline(6, 4, 2, 6, 4, 7, westStairs, westStairs, false);
        finder.fillWithOutline(0, 4, 8, 6, 4, 8, southStairs, southStairs, false);
        finder.addBlock(northStairs.with(StairsBlock.SHAPE, StairShape.OUTER_RIGHT), 0, 4, 1);
        finder.addBlock(northStairs.with(StairsBlock.SHAPE, StairShape.OUTER_LEFT), 6, 4, 1);
        finder.addBlock(southStairs.with(StairsBlock.SHAPE, StairShape.OUTER_LEFT), 0, 4, 8);
        finder.addBlock(southStairs.with(StairsBlock.SHAPE, StairShape.OUTER_RIGHT), 6, 4, 8);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new SwampHutFinder(world, chunkPos));
        return finders;
    }

}
