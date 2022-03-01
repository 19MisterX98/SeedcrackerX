package kaptainwutax.seedcrackerX.finder.decorator;

import com.seedfinding.mccore.version.MCVersion;
import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.cracker.decorator.Decorator;
import kaptainwutax.seedcrackerX.cracker.decorator.Dungeon;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.finder.BlockFinder;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.render.Color;
import kaptainwutax.seedcrackerX.render.Cube;
import kaptainwutax.seedcrackerX.render.Cuboid;
import kaptainwutax.seedcrackerX.util.BiomeFixer;
import kaptainwutax.seedcrackerX.util.PosIterator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DungeonFinder extends BlockFinder {

    protected static Set<BlockPos> POSSIBLE_FLOOR_POSITIONS = PosIterator.create(
            new BlockPos(-4, -1, -4),
            new BlockPos(4, -1, 4)
    );


    public DungeonFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.SPAWNER);
        this.searchPositions = CHUNK_POSITIONS;
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();

        for (int chunkX = chunkPos.x - 1; chunkX <= chunkPos.x + 1; chunkX++) {
            for (int chunkZ = chunkPos.z - 1; chunkZ <= chunkPos.z + 1; chunkZ++) {
                if (surroundingChunksLoaded(chunkX, chunkZ, world)) {
                    finders.add(new DungeonFinder(world, new ChunkPos(chunkX, chunkZ)));
                }
            }
        }

        return finders;
    }

    private static boolean surroundingChunksLoaded(int chunkX, int chunkZ, World world) {
        for (int x = chunkX - 1; x <= chunkX + 1; x++) {
            for (int z = chunkZ - 1; z <= chunkZ + 1; z++) {
                if (world.getChunkManager().getChunk(x, z) == null) return false;
            }
        }
        return true;
    }

    private boolean AntiXRay(BlockPos pos) {
        Set<BlockPos> XRAY_TEST_POS = new HashSet<>();
        XRAY_TEST_POS.add(new BlockPos(4, 0, 0));
        XRAY_TEST_POS.add(new BlockPos(3, 0, 0));
        XRAY_TEST_POS.add(new BlockPos(-4, 0, 0));
        XRAY_TEST_POS.add(new BlockPos(-3, 0, 0));
        XRAY_TEST_POS.add(new BlockPos(0, 0, 4));
        XRAY_TEST_POS.add(new BlockPos(0, 0, 3));
        XRAY_TEST_POS.add(new BlockPos(0, 0, -4));
        XRAY_TEST_POS.add(new BlockPos(0, 0, -3));
        for (BlockPos blockpos : XRAY_TEST_POS) {
            BlockPos.Mutable currentPos = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
            currentPos.move(blockpos);
            Block posCheck = this.world.getBlockState(currentPos).getBlock();
            if (posCheck == Blocks.COBBLESTONE) {
                currentPos.move(0, -1, 0);
                posCheck = this.world.getBlockState(currentPos).getBlock();
                if (posCheck != Blocks.COBBLESTONE && posCheck != Blocks.MOSSY_COBBLESTONE) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<BlockPos> findInChunk() {
        //Gets all the positions with a mob spawner in the chunk.
        List<BlockPos> result = super.findInChunk();

        if (result.size() != 1) return new ArrayList<>();

        result.removeIf(pos -> {

            BlockEntity blockEntity = this.world.getBlockEntity(pos);
            if (!(blockEntity instanceof MobSpawnerBlockEntity)) return true;
            int count = 0;
            for (BlockPos blockPos : POSSIBLE_FLOOR_POSITIONS) {
                BlockPos currentPos = pos.add(blockPos);
                Block currentBlock = this.world.getBlockState(currentPos).getBlock();
                if (currentBlock == Blocks.COBBLESTONE || currentBlock == Blocks.MOSSY_COBBLESTONE) {
                    count++;
                }
            }
            return count < 20;
        });

        if (result.size() != 1) return new ArrayList<>();
        Biome biome = this.world.getBiomeForNoiseGen((this.chunkPos.x << 2) + 2, 0, (this.chunkPos.z << 2) + 2).value();

        BlockPos pos = result.get(0);
        if (Config.get().getVersion().isNewerThan(MCVersion.v1_17_1)) {
            Decorator.Data<?> data;
            if (pos.getY() < 0) {
                data = Features.DEEP_DUNGEON.at(pos.getX(), pos.getY(), pos.getZ(), BiomeFixer.swap(biome));
            } else {
                data = Features.DUNGEON.at(pos.getX(), pos.getY(), pos.getZ(), null, null, BiomeFixer.swap(biome), null);
            }
            this.renderers.add(new Cube(pos, new Color(255, 0, 0)));
            SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_BIOMES);
            return result;
        }

        Vec3i size = this.getDungeonSize(pos);

        int[] floorCalls = this.getFloorCalls(size, pos);
        Dungeon.Data data = Features.DUNGEON.at(pos.getX(), pos.getY(), pos.getZ(), size, floorCalls, BiomeFixer.swap(biome), heightContext);
        if (AntiXRay(pos) && Config.get().antiXrayBypass) {
            if (SeedCracker.get().getDataStorage().baseSeedData.contains(new DataStorage.Entry<>(data, null))) {
                return result;
            }
            this.renderers.add(new Cube(pos, new Color(255, 0, 0)));
            Thread floorCallsUpdater = new Thread(() -> {
                try {
                    //server needs to send the blocks before we do a second check
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int[] floorCallsAfter = this.getFloorCalls(size, pos);
                for (int i = 0; i < floorCallsAfter.length; i++) {
                    if (floorCallsAfter[i] != 2) {
                        floorCalls[i] = floorCallsAfter[i];
                    }
                }

                if (SeedCracker.get().getDataStorage().addBaseData(data, data::onDataAdded)) {
                    if (data.usesFloor()) {
                        this.renderers.add(new Cuboid(pos.subtract(size), pos.add(size).add(1, -1, 1), new Color(255, 0, 0)));
                    }
                } else {
                    this.renderers.clear();
                }

            });
            blockUpdateExploit(pos, size, floorCallsUpdater);
        } else if (SeedCracker.get().getDataStorage().addBaseData(data, data::onDataAdded)) {
            this.renderers.add(new Cube(pos, new Color(255, 0, 0)));

            if (data.usesFloor()) {
                this.renderers.add(new Cuboid(pos.subtract(size), pos.add(size).add(1, -1, 1), new Color(255, 0, 0)));
            }
        }
        return result;
    }

    public void blockUpdateExploit(BlockPos pos, Vec3i size, Thread startCracker) {
        ArrayList<BlockPos> floorBlocks = new ArrayList<>();
        for (int xo = -size.getX(); xo <= size.getX(); xo++) {
            for (int zo = -size.getZ(); zo <= size.getZ(); zo++) {
                floorBlocks.add(pos.add(xo, -1, zo));
            }
        }
        SeedCracker.get().getDataStorage().blockUpdateQueue.add(floorBlocks, pos, startCracker);
    }

    public Vec3i getDungeonSize(BlockPos spawnerPos) {

        int x = PosIterator.create(spawnerPos.add(4, 3, -4), spawnerPos.add(4, 3, 4)).stream().filter(pos ->
                world.getBlockState(pos).getBlock() == Blocks.COBBLESTONE).count() > 2 ? 4 : 3;

        int z = PosIterator.create(spawnerPos.add(-4, 3, 4), spawnerPos.add(4, 3, 4)).stream().filter(pos ->
                world.getBlockState(pos).getBlock() == Blocks.COBBLESTONE).count() > 2 ? 4 : 3;

        return new Vec3i(x, 0, z);
    }

    public int[] getFloorCalls(Vec3i dungeonSize, BlockPos spawnerPos) {
        int[] floorCalls = new int[(dungeonSize.getX() * 2 + 1) * (dungeonSize.getZ() * 2 + 1)];
        int i = 0;

        for (int xo = -dungeonSize.getX(); xo <= dungeonSize.getX(); xo++) {
            for (int zo = -dungeonSize.getZ(); zo <= dungeonSize.getZ(); zo++) {
                Block block = this.world.getBlockState(spawnerPos.add(xo, -1, zo)).getBlock();
                if (block == Blocks.MOSSY_COBBLESTONE) {
                    floorCalls[i++] = Dungeon.Data.MOSSY_COBBLESTONE_CALL;
                } else if (block == Blocks.COBBLESTONE) {
                    floorCalls[i++] = Dungeon.Data.COBBLESTONE_CALL;
                } else if (block != Blocks.AIR && block != Blocks.CAVE_AIR) {
                    floorCalls[i++] = 2;
                } else {
                    floorCalls[i++] = 3;
                }
            }
        }

        return floorCalls;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

}
