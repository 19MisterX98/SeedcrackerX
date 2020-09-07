package kaptainwutax.seedcracker.finder.decorator;

import kaptainwutax.seedcracker.Features;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.decorator.Dungeon;
import kaptainwutax.seedcracker.finder.BlockFinder;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Color;
import kaptainwutax.seedcracker.render.Cube;
import kaptainwutax.seedcracker.render.Cuboid;
import kaptainwutax.seedcracker.util.BiomeFixer;
import kaptainwutax.seedcracker.util.Log;
import kaptainwutax.seedcracker.util.PosIterator;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DungeonFinder extends BlockFinder {

    protected static List<BlockPos> SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> {
        return false;
    });

    protected static Set<BlockPos> REQUIRED_FLOOR_POSITIONS = PosIterator.create(
            new BlockPos(-3, -1, -3),
            new BlockPos(3, -1, 3)
    );

    public DungeonFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.SPAWNER);
        this.searchPositions = SEARCH_POSITIONS;
    }

    @Override
    public List<BlockPos> findInChunk() {
        //Gets all the positions with a mob spawner in the chunk.
        List<BlockPos> result = super.findInChunk();

        if(result.size() != 1)return new ArrayList<>();

        result.removeIf(pos -> {
            BlockEntity blockEntity = this.world.getBlockEntity(pos);
            if(!(blockEntity instanceof MobSpawnerBlockEntity))return true;

            for(BlockPos blockPos: REQUIRED_FLOOR_POSITIONS) {
                BlockPos currentPos = pos.add(blockPos);
                Block currentBlock = this.world.getBlockState(currentPos).getBlock();

                if(currentBlock == Blocks.COBBLESTONE) {}
                else if(currentBlock == Blocks.MOSSY_COBBLESTONE) {}
                else return true;
            }

            return false;
        });

        if(result.size() != 1)return new ArrayList<>();
        Biome biome = this.world.getBiomeForNoiseGen((this.chunkPos.x << 2) + 2, 0, (this.chunkPos.z << 2) + 2);

        BlockPos pos = result.get(0);
        Vec3i size = this.getDungeonSize(pos);
        int[] floorCalls = this.getFloorCalls(size, pos);

        Dungeon.Data data = Features.DUNGEON.at(pos.getX(), pos.getY(), pos.getZ(), size, floorCalls, BiomeFixer.swap(biome));

        if(SeedCracker.get().getDataStorage().addBaseData(data, data::onDataAdded)) {
            this.renderers.add(new Cube(pos, new Color(255, 0, 0)));

            if(data.usesFloor()) {
                this.renderers.add(new Cuboid(pos.subtract(size), pos.add(size).add(1, -1, 1), new Color(255, 0, 0)));
            }
        }

        return result;
    }

    public Vec3i getDungeonSize(BlockPos spawnerPos) {
        for(int xo = 4; xo >= 3; xo--) {
            for(int zo = 4; zo >= 3; zo--) {
                Block block = this.world.getBlockState(spawnerPos.add(xo, -1, zo)).getBlock();
                if(block == Blocks.MOSSY_COBBLESTONE || block == Blocks.COBBLESTONE)return new Vec3i(xo, 0, zo);
            }
        }

        return Vec3i.ZERO;
    }

    public int[] getFloorCalls(Vec3i dungeonSize, BlockPos spawnerPos) {
        int[] floorCalls = new int[(dungeonSize.getX() * 2 + 1) * (dungeonSize.getZ() * 2 + 1)];
        int i = 0;

        for(int xo = -dungeonSize.getX(); xo <= dungeonSize.getX(); xo++) {
            for(int zo = -dungeonSize.getZ(); zo <= dungeonSize.getZ(); zo++) {
                Block block = this.world.getBlockState(spawnerPos.add(xo, -1, zo)).getBlock();

                if(block == Blocks.MOSSY_COBBLESTONE) {
                    floorCalls[i++] = Dungeon.Data.MOSSY_COBBLESTONE_CALL;
                } else if(block == Blocks.COBBLESTONE) {
                    floorCalls[i++] = Dungeon.Data.COBBLESTONE_CALL;
                } else {
                    return null;
                }
            }
        }
        
        return floorCalls;
    }
    

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new DungeonFinder(world, chunkPos));

        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z)));
        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x, chunkPos.z - 1)));
        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));

        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z)));
        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x, chunkPos.z + 1)));
        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x + 1, chunkPos.z + 1)));

        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z - 1)));
        finders.add(new DungeonFinder(world, new ChunkPos(chunkPos.x - 1, chunkPos.z + 1)));
        return finders;
    }

}
