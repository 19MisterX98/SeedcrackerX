package kaptainwutax.seedcracker.finder.structure;

import kaptainwutax.featureutils.structure.RegionStructure;
import kaptainwutax.seedcracker.Features;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.DataAddedEvent;
import kaptainwutax.seedcracker.finder.BlockFinder;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Color;
import kaptainwutax.seedcracker.render.Cube;
import kaptainwutax.seedcracker.render.Cuboid;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.ArrayList;
import java.util.List;

public class ShipwreckFinder extends BlockFinder {

    protected static List<BlockPos> SEARCH_POSITIONS = Finder.buildSearchPositions(Finder.CHUNK_POSITIONS, pos -> {
        return false;
    });

    public ShipwreckFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.CHEST);
        this.searchPositions = SEARCH_POSITIONS;
    }

    @Override
    public List<BlockPos> findInChunk() {
        Biome biome = this.world.getBiomeForNoiseGen((this.chunkPos.x << 2) + 2, 0, (this.chunkPos.z << 2) + 2);

        if(!biome.getGenerationSettings().hasStructureFeature(StructureFeature.SHIPWRECK)) {
            return new ArrayList<>();
        }

        List<BlockPos> result = super.findInChunk();

        result.removeIf(pos -> {
            BlockState state = this.world.getBlockState(pos);
            if(state.get(ChestBlock.CHEST_TYPE) != ChestType.SINGLE)return true;

            BlockEntity blockEntity = this.world.getBlockEntity(pos);
            if(!(blockEntity instanceof ChestBlockEntity))return true;

            return !this.onChestFound(pos);
        });

        return result;
    }

    /**
     * Source: https://github.com/skyrising/casual-mod/blob/master/src/main/java/de/skyrising/casual/ShipwreckFinder.java
     * */
    private boolean onChestFound(BlockPos pos) {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
        Direction chestFacing = world.getBlockState(pos).get(ChestBlock.FACING);

        int[] stairs = new int[4];
        int totalStairs = 0;
        int[] trapdoors = new int[4];
        int totalTrapdoors = 0;
        for(int y = -1; y <= 2; y++) {
            for(int x = -1; x <= 1; x++) {
                for(int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0)continue;
                    mutablePos.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    BlockState neighborState = world.getBlockState(mutablePos);
                    Block neighborBlock = neighborState.getBlock();
                    if(neighborBlock == Blocks.VOID_AIR)return false;

                    if(neighborBlock instanceof StairsBlock) {
                        stairs[y + 1]++;
                        totalStairs++;
                    } else if(neighborBlock instanceof TrapdoorBlock) {
                        trapdoors[y + 1]++;
                        totalTrapdoors++;
                    }
                }
            }
        }
        //System.out.printf("%s: chest facing %s\n", pos, chestFacing);
        int chestX = 4;
        int chestY = 2;
        int chestZ = 0;
        int length = 16;
        int height = 9;
        Direction direction = chestFacing;

        if(trapdoors[3] > 4) { // with_mast[_degraded]
            chestZ = 9;
            height = 21;
            length = 28;
        } else if(totalTrapdoors == 0 && stairs[3] == 3) { // upsidedown_backhalf[_degraded]
            if(stairs[0] == 0) {
                chestX = 2;
                chestZ = 12;
                direction = chestFacing.getOpposite();
            } else { // redundant
                chestX = 3;
                chestY = 5;
                chestZ = 5;
                direction = chestFacing.rotateYClockwise();
            }
        } else if(totalTrapdoors == 0) { // rightsideup that have backhalf
            if(stairs[0] == 4) {
                if(totalStairs > 4) {
                    chestX = 6;
                    chestY = 4;
                    chestZ = 12;
                    direction = chestFacing.getOpposite();
                } else { // sideways backhalf
                    chestX = 6;
                    chestY = 3;
                    chestZ = 8;
                    length = 17;
                    direction = chestFacing.getOpposite();
                }
            } else if(stairs[0] == 3 && totalStairs > 5) {
                chestX = 5;
                chestZ = 6;
                direction = chestFacing.rotateYCounterclockwise();
            }

            mutablePos.set(pos);
            mutablePos.move(0, -chestY, 0);
            mutablePos.move(direction.rotateYClockwise(), chestX - 4);
            mutablePos.move(direction, -chestZ - 1);

            if(this.world.getBlockState(mutablePos).getMaterial() == Material.WOOD) {
                if(length == 17) { // sideways
                    chestZ += 11;
                    length += 11;
                } else {
                    chestZ += 12;
                    length += 12;
                }
                mutablePos.move(0, 10, 0);

                BlockState b = this.world.getBlockState(mutablePos);

                if(this.world.getBlockState(mutablePos).isIn(BlockTags.LOGS)) {
                    height = 21;
                }
            }
        } else if(totalTrapdoors == 2 && trapdoors[3] == 2 && stairs[3] == 3) { // rightsideup_fronthalf[_degraded]
            chestZ = 8;
            length = 24;
        }

        if(chestZ != 0) {
            mutablePos.set(pos);
            mutablePos.move(direction, 15 - chestZ);
            mutablePos.move(direction.rotateYClockwise(), chestX - 4);
            BlockPos.Mutable pos2 = new BlockPos.Mutable(mutablePos.getX(), mutablePos.getY(), mutablePos.getZ());
            pos2.move(0, -chestY, 0);
            pos2.move(direction, -15);
            pos2.move(direction.rotateYClockwise(), 4);
            BlockPos.Mutable pos3 = new BlockPos.Mutable(pos2.getX(), pos2.getY(), pos2.getZ());
            pos3.move(direction, length - 1);
            pos3.move(direction.rotateYClockwise(), -8);
            pos3.move(0, height - 1, 0);

            BlockBox box = new BlockBox(
                    Math.min(pos2.getX(), pos3.getX()), pos2.getY(), Math.min(pos2.getZ(), pos3.getZ()),
                    Math.max(pos2.getX(), pos3.getX()) + 1, pos3.getY() + 1, Math.max(pos2.getZ(), pos3.getZ()) + 1);

            mutablePos.move(-4, -chestY, -15);

            if((mutablePos.getX() & 0xf) == 0 && (mutablePos.getZ() & 0xf) == 0) {
                RegionStructure.Data<?> data = Features.SHIPWRECK.at(new ChunkPos(mutablePos).x, new ChunkPos(mutablePos).z);

                if(SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_STRUCTURES)) {
                    this.renderers.add(new Cuboid(box, new Color(0, 255, 255)));
                    this.renderers.add(new Cube(new ChunkPos(mutablePos).getStartPos().offset(Direction.UP, mutablePos.getY()), new Color(0, 255, 255)));
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new ShipwreckFinder(world, chunkPos));

        finders.add(new ShipwreckFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z)));
        finders.add(new ShipwreckFinder(world, new ChunkPos(chunkPos.x, chunkPos.z - 1)));
        finders.add(new ShipwreckFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));

        finders.add(new ShipwreckFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z)));
        finders.add(new ShipwreckFinder(world, new ChunkPos(chunkPos.x, chunkPos.z + 1)));
        finders.add(new ShipwreckFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z + 1)));

        finders.add(new ShipwreckFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));
        finders.add(new ShipwreckFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z + 1)));
        return finders;
    }

}
