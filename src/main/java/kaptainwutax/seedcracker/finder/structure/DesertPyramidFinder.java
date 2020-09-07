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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DesertPyramidFinder extends AbstractTempleFinder {
    
    public DesertPyramidFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, new Vec3i(21, 15, 21));
    }

    @Override
    public List<BlockPos> findInChunk() {
        Map<PieceFinder, List<BlockPos>> result = super.findInChunkPieces();
        List<BlockPos> combinedResult = new ArrayList<>();

        result.forEach((pieceFinder, positions) -> {
            combinedResult.addAll(positions);

            positions.forEach(pos -> {
                RegionStructure.Data<?> data = Features.DESERT_PYRAMID.at(this.chunkPos.x, this.chunkPos.z);

                if(SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_STRUCTURES)) {
                    this.addRenderers(pieceFinder, pos, new Color(255, 0, 255));
                }
            });
        });

        return combinedResult;
    }

    @Override
    protected StructureFeature<?> getStructureFeature() {
        return StructureFeature.DESERT_PYRAMID;
    }

    @Override
    public void buildStructure(PieceFinder finder) {
        BlockState blockState_1 = Blocks.SANDSTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH);
        BlockState blockState_2 = Blocks.SANDSTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH);
        BlockState blockState_3 = Blocks.SANDSTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST);
        BlockState blockState_4 = Blocks.SANDSTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST);
        finder.fillWithOutline(0, 0, 0, 4, 9, 4, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.fillWithOutline(1, 10, 1, 3, 10, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
        finder.addBlock(blockState_1, 2, 10, 0);
        finder.addBlock(blockState_2, 2, 10, 4);
        finder.addBlock(blockState_3, 0, 10, 2);
        finder.addBlock(blockState_4, 4, 10, 2);
        finder.fillWithOutline(finder.width - 5, 0, 0, finder.width - 1, 9, 4, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.fillWithOutline(finder.width - 4, 10, 1, finder.width - 2, 10, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
        finder.addBlock(blockState_1, finder.width - 3, 10, 0);
        finder.addBlock(blockState_2, finder.width - 3, 10, 4);
        finder.addBlock(blockState_3, finder.width - 5, 10, 2);
        finder.addBlock(blockState_4, finder.width - 1, 10, 2);
        finder.fillWithOutline(8, 0, 0, 12, 4, 4, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.fillWithOutline(9, 1, 0, 11, 3, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 9, 1, 1);
        finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 9, 2, 1);
        finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 9, 3, 1);
        finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 10, 3, 1);
        finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 11, 3, 1);
        finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 11, 2, 1);
        finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 11, 1, 1);
        finder.fillWithOutline(4, 1, 1, 8, 3, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.fillWithOutline(4, 1, 2, 8, 2, 2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.fillWithOutline(12, 1, 1, 16, 3, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.fillWithOutline(12, 1, 2, 16, 2, 2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.fillWithOutline(5, 4, 5, finder.width - 6, 4, finder.depth - 6, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(9, 4, 9, 11, 4, 11, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.fillWithOutline(8, 1, 8, 8, 3, 8, Blocks.CUT_SANDSTONE.getDefaultState(), Blocks.CUT_SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(12, 1, 8, 12, 3, 8, Blocks.CUT_SANDSTONE.getDefaultState(), Blocks.CUT_SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(8, 1, 12, 8, 3, 12, Blocks.CUT_SANDSTONE.getDefaultState(), Blocks.CUT_SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(12, 1, 12, 12, 3, 12, Blocks.CUT_SANDSTONE.getDefaultState(), Blocks.CUT_SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(1, 1, 5, 4, 4, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(finder.width - 5, 1, 5, finder.width - 2, 4, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(6, 7, 9, 6, 7, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(finder.width - 7, 7, 9, finder.width - 7, 7, 11, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(5, 5, 9, 5, 7, 11, Blocks.CUT_SANDSTONE.getDefaultState(), Blocks.CUT_SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(finder.width - 6, 5, 9, finder.width - 6, 7, 11, Blocks.CUT_SANDSTONE.getDefaultState(), Blocks.CUT_SANDSTONE.getDefaultState(), false);
        finder.addBlock(Blocks.AIR.getDefaultState(), 5, 5, 10);
        finder.addBlock(Blocks.AIR.getDefaultState(), 5, 6, 10);
        finder.addBlock(Blocks.AIR.getDefaultState(), 6, 6, 10);
        finder.addBlock(Blocks.AIR.getDefaultState(), finder.width - 6, 5, 10);
        finder.addBlock(Blocks.AIR.getDefaultState(), finder.width - 6, 6, 10);
        finder.addBlock(Blocks.AIR.getDefaultState(), finder.width - 7, 6, 10);
        finder.fillWithOutline(2, 4, 4, 2, 6, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.fillWithOutline(finder.width - 3, 4, 4, finder.width - 3, 6, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.addBlock(blockState_1, 2, 4, 5);
        finder.addBlock(blockState_1, 2, 3, 4);
        finder.addBlock(blockState_1, finder.width - 3, 4, 5);
        finder.addBlock(blockState_1, finder.width - 3, 3, 4);
        finder.fillWithOutline(1, 1, 3, 2, 2, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(finder.width - 3, 1, 3, finder.width - 2, 2, 3, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
        finder.addBlock(Blocks.SANDSTONE.getDefaultState(), 1, 1, 2);
        finder.addBlock(Blocks.SANDSTONE.getDefaultState(), finder.width - 2, 1, 2);
        finder.addBlock(Blocks.SANDSTONE_SLAB.getDefaultState(), 1, 2, 2);
        finder.addBlock(Blocks.SANDSTONE_SLAB.getDefaultState(), finder.width - 2, 2, 2);
        finder.addBlock(blockState_4, 2, 1, 2);
        finder.addBlock(blockState_3, finder.width - 3, 1, 2);
        finder.fillWithOutline(4, 3, 5, 4, 3, 17, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(finder.width - 5, 3, 5, finder.width - 5, 3, 17, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(3, 1, 5, 4, 2, 16, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.fillWithOutline(finder.width - 6, 1, 5, finder.width - 5, 2, 16, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

        int int_7;
        for(int_7 = 5; int_7 <= 17; int_7 += 2) {
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 4, 1, int_7);
            finder.addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), 4, 2, int_7);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), finder.width - 5, 1, int_7);
            finder.addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), finder.width - 5, 2, int_7);
        }

        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 10, 0, 7);
        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 10, 0, 8);
        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 9, 0, 9);
        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 11, 0, 9);
        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 8, 0, 10);
        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 12, 0, 10);
        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 7, 0, 10);
        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 13, 0, 10);
        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 9, 0, 11);
        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 11, 0, 11);
        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 10, 0, 12);
        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 10, 0, 13);
        finder.addBlock(Blocks.BLUE_TERRACOTTA.getDefaultState(), 10, 0, 10);

        for(int_7 = 0; int_7 <= finder.width - 1; int_7 += finder.width - 1) {
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7, 2, 1);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 2, 2);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7, 2, 3);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7, 3, 1);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 3, 2);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7, 3, 3);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 4, 1);
            finder.addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), int_7, 4, 2);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 4, 3);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7, 5, 1);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 5, 2);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7, 5, 3);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 6, 1);
            finder.addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), int_7, 6, 2);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 6, 3);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 7, 1);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 7, 2);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 7, 3);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7, 8, 1);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7, 8, 2);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7, 8, 3);
        }

        for(int_7 = 2; int_7 <= finder.width - 3; int_7 += finder.width - 3 - 2) {
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7 - 1, 2, 0);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 2, 0);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7 + 1, 2, 0);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7 - 1, 3, 0);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 3, 0);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7 + 1, 3, 0);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7 - 1, 4, 0);
            finder.addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), int_7, 4, 0);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7 + 1, 4, 0);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7 - 1, 5, 0);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 5, 0);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7 + 1, 5, 0);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7 - 1, 6, 0);
            finder.addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), int_7, 6, 0);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7 + 1, 6, 0);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7 - 1, 7, 0);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7, 7, 0);
            finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), int_7 + 1, 7, 0);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7 - 1, 8, 0);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7, 8, 0);
            finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), int_7 + 1, 8, 0);
        }

        finder.fillWithOutline(8, 4, 0, 12, 6, 0, Blocks.CUT_SANDSTONE.getDefaultState(), Blocks.CUT_SANDSTONE.getDefaultState(), false);
        finder.addBlock(Blocks.AIR.getDefaultState(), 8, 6, 0);
        finder.addBlock(Blocks.AIR.getDefaultState(), 12, 6, 0);
        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 9, 5, 0);
        finder.addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), 10, 5, 0);
        finder.addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 11, 5, 0);
        finder.fillWithOutline(8, -14, 8, 12, -11, 12, Blocks.CUT_SANDSTONE.getDefaultState(), Blocks.CUT_SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(8, -10, 8, 12, -10, 12, Blocks.CHISELED_SANDSTONE.getDefaultState(), Blocks.CHISELED_SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(8, -9, 8, 12, -9, 12, Blocks.CUT_SANDSTONE.getDefaultState(), Blocks.CUT_SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(8, -8, 8, 12, -1, 12, Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false);
        finder.fillWithOutline(9, -11, 9, 11, -1, 11, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.addBlock(Blocks.STONE_PRESSURE_PLATE.getDefaultState(), 10, -11, 10);
        finder.fillWithOutline(9, -13, 9, 11, -13, 11, Blocks.TNT.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        finder.addBlock(Blocks.AIR.getDefaultState(), 8, -11, 10);
        finder.addBlock(Blocks.AIR.getDefaultState(), 8, -10, 10);
        finder.addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), 7, -10, 10);
        finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 7, -11, 10);
        finder.addBlock(Blocks.AIR.getDefaultState(), 12, -11, 10);
        finder.addBlock(Blocks.AIR.getDefaultState(), 12, -10, 10);
        finder.addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), 13, -10, 10);
        finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 13, -11, 10);
        finder.addBlock(Blocks.AIR.getDefaultState(), 10, -11, 8);
        finder.addBlock(Blocks.AIR.getDefaultState(), 10, -10, 8);
        finder.addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), 10, -10, 7);
        finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 10, -11, 7);
        finder.addBlock(Blocks.AIR.getDefaultState(), 10, -11, 12);
        finder.addBlock(Blocks.AIR.getDefaultState(), 10, -10, 12);
        finder.addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), 10, -10, 13);
        finder.addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 10, -11, 13);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new DesertPyramidFinder(world, chunkPos));
        finders.add(new DesertPyramidFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z)));
        finders.add(new DesertPyramidFinder(world, new ChunkPos(chunkPos.x, chunkPos.z - 1)));
        finders.add(new DesertPyramidFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));
        return finders;
    }
    
}
