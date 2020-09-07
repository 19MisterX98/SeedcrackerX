package kaptainwutax.seedcracker.finder.structure;

import kaptainwutax.featureutils.structure.RegionStructure;
import kaptainwutax.seedcracker.Features;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.DataAddedEvent;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Color;
import net.minecraft.block.*;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JunglePyramidFinder extends AbstractTempleFinder {

    public JunglePyramidFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, new Vec3i(12, 10, 15));
    }

    @Override
    public List<BlockPos> findInChunk() {
        Map<PieceFinder, List<BlockPos>> result = super.findInChunkPieces();
        List<BlockPos> combinedResult = new ArrayList<>();

        result.forEach((pieceFinder, positions) -> {
            combinedResult.addAll(positions);

            positions.forEach(pos -> {
                RegionStructure.Data<?> data = Features.JUNGLE_PYRAMID.at(this.chunkPos.x, this.chunkPos.z);

                if(SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_STRUCTURES)) {
                    this.addRenderers(pieceFinder, pos, new Color(255, 0, 255));
                }
            });
        });

        return combinedResult;
    }

    @Override
    protected StructureFeature<?> getStructureFeature() {
        return StructureFeature.JUNGLE_PYRAMID;
    }

    @Override
    public void buildStructure(PieceFinder finder) {
        BlockState eastStairs = Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST);
        BlockState westStairs = Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST);
        BlockState southStairs = Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH);
        BlockState northStairs = Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH);
        finder.addBlock(northStairs, 5, 9, 6);
        finder.addBlock(northStairs, 6, 9, 6);
        finder.addBlock(southStairs, 5, 9, 8);
        finder.addBlock(southStairs, 6, 9, 8);
        finder.addBlock(northStairs, 4, 0, 0);
        finder.addBlock(northStairs, 5, 0, 0);
        finder.addBlock(northStairs, 6, 0, 0);
        finder.addBlock(northStairs, 7, 0, 0);
        finder.addBlock(northStairs, 4, 1, 8);
        finder.addBlock(northStairs, 4, 2, 9);
        finder.addBlock(northStairs, 4, 3, 10);
        finder.addBlock(northStairs, 7, 1, 8);
        finder.addBlock(northStairs, 7, 2, 9);
        finder.addBlock(northStairs, 7, 3, 10);
        finder.addBlock(eastStairs, 4, 4, 5);
        finder.addBlock(westStairs, 7, 4, 5);
        finder.addBlock((Blocks.TRIPWIRE_HOOK.getDefaultState().with(TripwireHookBlock.FACING, Direction.EAST)).with(TripwireHookBlock.ATTACHED, true), 1, -3, 8);
        finder.addBlock((Blocks.TRIPWIRE_HOOK.getDefaultState().with(TripwireHookBlock.FACING, Direction.WEST)).with(TripwireHookBlock.ATTACHED, true), 4, -3, 8);
        finder.addBlock(((Blocks.TRIPWIRE.getDefaultState().with(TripwireBlock.EAST, true)).with(TripwireBlock.WEST, true)).with(TripwireBlock.ATTACHED, true), 2, -3, 8);
        finder.addBlock(((Blocks.TRIPWIRE.getDefaultState().with(TripwireBlock.EAST, true)).with(TripwireBlock.WEST, true)).with(TripwireBlock.ATTACHED, true), 3, -3, 8);
        BlockState blockState_5 = (Blocks.REDSTONE_WIRE.getDefaultState().with(RedstoneWireBlock.WIRE_CONNECTION_NORTH, WireConnection.SIDE)).with(RedstoneWireBlock.WIRE_CONNECTION_SOUTH, WireConnection.SIDE);
        finder.addBlock(Blocks.REDSTONE_WIRE.getDefaultState().with(RedstoneWireBlock.WIRE_CONNECTION_SOUTH, WireConnection.SIDE), 5, -3, 7);
        finder.addBlock(blockState_5, 5, -3, 6);
        finder.addBlock(blockState_5, 5, -3, 5);
        finder.addBlock(blockState_5, 5, -3, 4);
        finder.addBlock(blockState_5, 5, -3, 3);
        finder.addBlock(blockState_5, 5, -3, 2);
        finder.addBlock((Blocks.REDSTONE_WIRE.getDefaultState().with(RedstoneWireBlock.WIRE_CONNECTION_NORTH, WireConnection.SIDE)).with(RedstoneWireBlock.WIRE_CONNECTION_WEST, WireConnection.SIDE), 5, -3, 1);
        finder.addBlock(Blocks.REDSTONE_WIRE.getDefaultState().with(RedstoneWireBlock.WIRE_CONNECTION_EAST, WireConnection.SIDE), 4, -3, 1);
        finder.addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 3, -3, 1);

        finder.addBlock(Blocks.VINE.getDefaultState().with(VineBlock.SOUTH, true), 3, -2, 2);
        finder.addBlock((Blocks.TRIPWIRE_HOOK.getDefaultState().with(TripwireHookBlock.FACING, Direction.NORTH)).with(TripwireHookBlock.ATTACHED, true), 7, -3, 1);
        finder.addBlock((Blocks.TRIPWIRE_HOOK.getDefaultState().with(TripwireHookBlock.FACING, Direction.SOUTH)).with(TripwireHookBlock.ATTACHED, true), 7, -3, 5);
        finder.addBlock(((Blocks.TRIPWIRE.getDefaultState().with(TripwireBlock.NORTH, true)).with(TripwireBlock.SOUTH, true)).with(TripwireBlock.ATTACHED, true), 7, -3, 2);
        finder.addBlock(((Blocks.TRIPWIRE.getDefaultState().with(TripwireBlock.NORTH, true)).with(TripwireBlock.SOUTH, true)).with(TripwireBlock.ATTACHED, true), 7, -3, 3);
        finder.addBlock(((Blocks.TRIPWIRE.getDefaultState().with(TripwireBlock.NORTH, true)).with(TripwireBlock.SOUTH, true)).with(TripwireBlock.ATTACHED, true), 7, -3, 4);
        finder.addBlock(Blocks.REDSTONE_WIRE.getDefaultState().with(RedstoneWireBlock.WIRE_CONNECTION_EAST, WireConnection.SIDE), 8, -3, 6);
        finder.addBlock((Blocks.REDSTONE_WIRE.getDefaultState().with(RedstoneWireBlock.WIRE_CONNECTION_WEST, WireConnection.SIDE)).with(RedstoneWireBlock.WIRE_CONNECTION_SOUTH, WireConnection.SIDE), 9, -3, 6);
        finder.addBlock((Blocks.REDSTONE_WIRE.getDefaultState().with(RedstoneWireBlock.WIRE_CONNECTION_NORTH, WireConnection.SIDE)).with(RedstoneWireBlock.WIRE_CONNECTION_SOUTH, WireConnection.UP), 9, -3, 5);
        finder.addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 9, -3, 4);
        finder.addBlock(Blocks.REDSTONE_WIRE.getDefaultState().with(RedstoneWireBlock.WIRE_CONNECTION_NORTH, WireConnection.SIDE), 9, -2, 4);

        finder.addBlock(Blocks.VINE.getDefaultState().with(VineBlock.EAST, true), 8, -1, 3);
        finder.addBlock(Blocks.VINE.getDefaultState().with(VineBlock.EAST, true), 8, -2, 3);

        finder.addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 9, -3, 2);
        finder.addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 8, -3, 1);
        finder.addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 4, -3, 5);
        finder.addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 5, -2, 5);
        finder.addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 5, -1, 5);
        finder.addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 6, -3, 5);
        finder.addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 7, -2, 5);
        finder.addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 7, -1, 5);
        finder.addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 8, -3, 5);
        finder.addBlock(Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 8, -2, 11);
        finder.addBlock(Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 9, -2, 11);
        finder.addBlock(Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 10, -2, 11);
        BlockState blockState_6 = (Blocks.LEVER.getDefaultState().with(LeverBlock.FACING, Direction.NORTH)).with(LeverBlock.FACE, WallMountLocation.WALL);
        finder.addBlock(blockState_6, 8, -2, 12);
        finder.addBlock(blockState_6, 9, -2, 12);
        finder.addBlock(blockState_6, 10, -2, 12);
        finder.addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 10, -2, 9);
        finder.addBlock(Blocks.REDSTONE_WIRE.getDefaultState().with(RedstoneWireBlock.WIRE_CONNECTION_NORTH, WireConnection.SIDE), 8, -2, 9);
        finder.addBlock(Blocks.REDSTONE_WIRE.getDefaultState().with(RedstoneWireBlock.WIRE_CONNECTION_SOUTH, WireConnection.SIDE), 8, -2, 10);
        finder.addBlock(Blocks.REDSTONE_WIRE.getDefaultState(), 10, -1, 9);
        finder.addBlock(Blocks.STICKY_PISTON.getDefaultState().with(PistonBlock.FACING, Direction.UP), 9, -2, 8);
        finder.addBlock(Blocks.STICKY_PISTON.getDefaultState().with(PistonBlock.FACING, Direction.WEST), 10, -2, 8);
        finder.addBlock(Blocks.STICKY_PISTON.getDefaultState().with(PistonBlock.FACING, Direction.WEST), 10, -1, 8);
        finder.addBlock(Blocks.REPEATER.getDefaultState().with(RepeaterBlock.FACING, Direction.NORTH), 10, -2, 10);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new JunglePyramidFinder(world, chunkPos));
        return finders;
    }
    
}
