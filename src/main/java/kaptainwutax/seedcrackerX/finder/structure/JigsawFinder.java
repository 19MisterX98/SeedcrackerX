package kaptainwutax.seedcrackerX.finder.structure;

import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JigsawFinder extends PieceFinder {
    public JigsawFinder(World world, ChunkPos chunkPos, Direction facing, Vec3i size) {
        super(world, chunkPos, facing, size);
    }

    public static Map<Direction, List<BlockPos>> getSearchPositions(int xRotation, int zRotation, int xOffset, int zOffset, Vec3i size) {
        Map<Direction, List<BlockPos>> positions = new HashMap<>();

        for(Direction direction : Direction.Type.HORIZONTAL) {
            positions.put(direction, new ArrayList<>());
            //this was painful and my hope is to never do it again
            int x = switch (direction) {
                case EAST -> xRotation-size.getZ()+1+zRotation-zOffset;
                case SOUTH -> xRotation-size.getX()+1+xRotation-xOffset;
                case WEST -> xRotation-zRotation+zOffset;
                default -> xOffset;
            };
            int z = switch (direction) {
                case EAST -> zRotation-xRotation+xOffset;
                case SOUTH -> zRotation-size.getZ()+1+zRotation-zOffset;
                case WEST -> zRotation-size.getX()+1+xRotation-xOffset;
                default -> zOffset;
            };
            if (x >= 0 && x < 16 && z >= 0 && z < 16 ) {
                int startIndex = heightContext.getHeight()*x*16+heightContext.getHeight()*z;
                for (int y = 0; y < heightContext.getHeight(); y++) {
                    positions.get(direction).add(CHUNK_POSITIONS.get(y+startIndex));
                }
            } else {
                for (int y = heightContext.getBottomY(); y < heightContext.getTopY(); y++) {
                    positions.get(direction).add(new BlockPos(x, y, z));
                }
            }
        }
        return positions;
    }


    @Override
    public void setOrientation(Direction facing) {
        this.facing = facing;
        this.mirror = BlockMirror.NONE;
        if (facing == null) {
            this.rotation = BlockRotation.NONE;
        } else {
            switch (facing) {
                case SOUTH -> this.rotation = BlockRotation.CLOCKWISE_180;
                case WEST -> this.rotation = BlockRotation.COUNTERCLOCKWISE_90;
                case EAST -> this.rotation = BlockRotation.CLOCKWISE_90;
                default -> this.rotation = BlockRotation.NONE;
            }
        }
    }

    @Override
    protected int applyXTransform(int x, int z) {
        if (this.facing == null) {
            return x;
        } else {
            return switch (this.facing) {
                case EAST -> -z + this.getLayout().getX()-1;
                case SOUTH -> -x + this.getLayout().getX()-1;
                case WEST -> z;

                default -> x;
            };
        }
    }

    @Override
    protected int applyZTransform(int x, int z) {
        if (this.facing == null) {
            return z;
        } else {
            return switch (this.facing) {
                case EAST -> x;
                case SOUTH -> -z + this.getLayout().getZ()-1;
                case WEST -> -x + this.getLayout().getZ()-1;

                default -> z;
            };
        }
    }

}
