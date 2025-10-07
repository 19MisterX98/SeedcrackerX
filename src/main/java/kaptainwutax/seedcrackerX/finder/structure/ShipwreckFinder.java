package kaptainwutax.seedcrackerX.finder.structure;

import com.seedfinding.mcfeature.structure.RegionStructure;
import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.finder.BlockFinder;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.render.Cuboid;
import kaptainwutax.seedcrackerX.util.BiomeFixer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class ShipwreckFinder extends BlockFinder {

    public ShipwreckFinder(Level world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.CHEST);
        this.searchPositions = CHUNK_POSITIONS;
    }

    public static List<Finder> create(Level world, ChunkPos chunkPos) {
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

    @Override
    public List<BlockPos> findInChunk() {
        Biome biome = this.world.getNoiseBiome((this.chunkPos.x << 2) + 2, 64, (this.chunkPos.z << 2) + 2).value();

        if (!Features.SHIPWRECK.isValidBiome(BiomeFixer.swap(biome))) {
            return new ArrayList<>();
        }

        List<BlockPos> result = super.findInChunk();

        result.removeIf(pos -> {
            BlockState state = this.world.getBlockState(pos);
            if (state.getValue(ChestBlock.TYPE) != ChestType.SINGLE) return true;

            BlockEntity blockEntity = this.world.getBlockEntity(pos);
            if (!(blockEntity instanceof ChestBlockEntity)) return true;

            return !this.onChestFound(pos);
        });

        return result;
    }

    /**
     * Source: https://github.com/skyrising/casual-mod/blob/master/src/main/java/de/skyrising/casual/ShipwreckFinder.java
     */
    private boolean onChestFound(BlockPos pos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        Direction chestFacing = world.getBlockState(pos).getValue(ChestBlock.FACING);

        int[] stairs = new int[4];
        int totalStairs = 0;
        int[] trapdoors = new int[4];
        int totalTrapdoors = 0;
        for (int y = -1; y <= 2; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    mutablePos.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    BlockState neighborState = world.getBlockState(mutablePos);
                    Block neighborBlock = neighborState.getBlock();
                    if (neighborBlock == Blocks.VOID_AIR) return false;

                    if (neighborBlock instanceof StairBlock) {
                        stairs[y + 1]++;
                        totalStairs++;
                    } else if (neighborBlock instanceof TrapDoorBlock) {
                        trapdoors[y + 1]++;
                        totalTrapdoors++;
                    }
                }
            }
        }
        int chestX = 4;
        int chestY = 2;
        int chestZ = 0;
        int length = 16;
        int height = 9;
        Direction direction = chestFacing;

        if (trapdoors[3] > 4) { // with_mast[_degraded]
            chestZ = 9;
            height = 21;
            length = 28;
        } else if (totalTrapdoors == 0 && stairs[3] == 3) { // upsidedown_backhalf[_degraded]
            if (stairs[0] == 0) {
                chestX = 2;
                chestZ = 12;
                direction = chestFacing.getOpposite();
            } else { // upsidedown front
                chestX = 3;
                chestY = 5;
                chestZ = 17;
                length = 22;
                direction = chestFacing.getClockWise();
            }
        } else if (totalTrapdoors == 0) { // rightsideup that have backhalf
            if (stairs[0] == 4) {
                if (totalStairs > 4) {
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
            } else if (stairs[0] == 3 && totalStairs > 5) {
                chestX = 5;
                chestZ = 6;
                direction = chestFacing.getCounterClockWise();
            } else if (totalStairs == 3 && stairs[0] == 1 && stairs[2] == 1 && stairs[3] == 1) { // upsidedown full
                chestX = 3;
                chestY = 5;
                chestZ = 17;
                length = 28;
                direction = chestFacing.getClockWise();
            }

            mutablePos.set(pos);
            mutablePos.move(0, -chestY, 0);
            mutablePos.move(direction.getClockWise(), chestX - 4);
            mutablePos.move(direction, -chestZ - 1);

            if (this.world.getBlockState(mutablePos).is(BlockTags.MINEABLE_WITH_AXE)) {
                if (length == 17) { // sideways
                    chestZ += 11;
                    length += 11;
                } else {
                    chestZ += 12;
                    length += 12;
                }
                mutablePos.move(0, 10, 0);

                if (this.world.getBlockState(mutablePos).is(BlockTags.LOGS)) {
                    height = 21;
                }
            }
        } else if (totalTrapdoors == 2 && trapdoors[3] == 2 && stairs[3] == 3) { // rightsideup_fronthalf[_degraded]
            chestZ = 8;
            length = 24;
        } else if (totalTrapdoors == 2 && totalStairs == 4 && stairs[0] == 2) {//sideways-fronthalf
            chestX = 5;
            chestY = 3;
            chestZ = 8;
            length = 24;
        }

        if (chestZ != 0) {
            mutablePos.set(pos);
            mutablePos.move(direction, 15 - chestZ);
            mutablePos.move(direction.getClockWise(), chestX - 4);
            BlockPos.MutableBlockPos pos2 = new BlockPos.MutableBlockPos(mutablePos.getX(), mutablePos.getY(), mutablePos.getZ());
            pos2.move(0, -chestY, 0);
            pos2.move(direction, -15);
            pos2.move(direction.getClockWise(), 4);
            BlockPos.MutableBlockPos pos3 = new BlockPos.MutableBlockPos(pos2.getX(), pos2.getY(), pos2.getZ());
            pos3.move(direction, length - 1);
            pos3.move(direction.getClockWise(), -8);
            pos3.move(0, height - 1, 0);

            BoundingBox box = new BoundingBox(
                    Math.min(pos2.getX(), pos3.getX()), pos2.getY(), Math.min(pos2.getZ(), pos3.getZ()),
                    Math.max(pos2.getX(), pos3.getX()) + 1, pos3.getY() + 1, Math.max(pos2.getZ(), pos3.getZ()) + 1);

            mutablePos.move(-4, -chestY, -15);

            if ((mutablePos.getX() & 0xf) == 0 && (mutablePos.getZ() & 0xf) == 0) {
                RegionStructure.Data<?> data = Features.SHIPWRECK.at(new ChunkPos(mutablePos).x, new ChunkPos(mutablePos).z);

                if (SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_LIFTING)) {
                    this.cuboids.add(new Cuboid(box, ARGB.color(0, 255, 255)));
                    this.cuboids.add(new Cuboid(new ChunkPos(mutablePos).getWorldPosition().relative(Direction.UP, mutablePos.getY()), ARGB.color(0, 255, 255)));
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

}
