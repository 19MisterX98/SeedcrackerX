package kaptainwutax.seedcracker.finder.structure;

import kaptainwutax.featureutils.structure.RegionStructure;
import kaptainwutax.seedcracker.Features;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.cracker.DataAddedEvent;
import kaptainwutax.seedcracker.finder.BlockFinder;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.render.Color;
import kaptainwutax.seedcracker.render.Cube;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.ArrayList;
import java.util.List;

public class BuriedTreasureFinder extends BlockFinder {

    protected static List<BlockPos> SEARCH_POSITIONS = buildSearchPositions(CHUNK_POSITIONS, pos -> {
        //Buried treasure chests always generate at (9, 9) within a chunk.
        int localX = pos.getX() & 15;
        int localZ = pos.getZ() & 15;
        if(localX != 9 || localZ != 9)return true;
        if(pos.getY() > 90)return true;
        return false;
    });

    protected static final List<BlockState> CHEST_HOLDERS = new ArrayList<>();

    static {
        CHEST_HOLDERS.add(Blocks.SANDSTONE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.STONE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.ANDESITE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.GRANITE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.DIORITE.getDefaultState());

        //Population can turn stone, andesite, granite and diorite into ores...
        CHEST_HOLDERS.add(Blocks.COAL_ORE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.IRON_ORE.getDefaultState());
        CHEST_HOLDERS.add(Blocks.GOLD_ORE.getDefaultState());

        //Ocean can turn stone into gravel.
        CHEST_HOLDERS.add(Blocks.GRAVEL.getDefaultState());
    }

    public BuriedTreasureFinder(World world, ChunkPos chunkPos) {
        super(world, chunkPos, Blocks.CHEST);
        this.searchPositions = SEARCH_POSITIONS;
    }

    @Override
    public List<BlockPos> findInChunk() {
        Biome biome = this.world.getBiomeForNoiseGen((this.chunkPos.x << 2) + 2, 0, (this.chunkPos.z << 2) + 2);
        if(!biome.getGenerationSettings().hasStructureFeature(StructureFeature.BURIED_TREASURE))return new ArrayList<>();

        List<BlockPos> result = super.findInChunk();

        result.removeIf(pos -> {
            BlockState chest = world.getBlockState(pos);
            if(chest.get(ChestBlock.WATERLOGGED))return true;

            BlockState chestHolder = world.getBlockState(pos.down());
            if(!CHEST_HOLDERS.contains(chestHolder))return true;

            return false;
        });

        result.forEach(pos -> {
            RegionStructure.Data<?> data = Features.BURIED_TREASURE.at(this.chunkPos.x, this.chunkPos.z);

            if(SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_STRUCTURES)) {
                this.renderers.add(new Cube(pos, new Color(255, 255, 0)));
            }
        });

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

    public static List<Finder> create(World world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new BuriedTreasureFinder(world, chunkPos));
        return finders;
    }

}
