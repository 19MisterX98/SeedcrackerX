package kaptainwutax.seedcrackerX.finder.structure;

import com.seedfinding.mcfeature.structure.RegionStructure;
import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.render.Cuboid;
import kaptainwutax.seedcrackerX.util.BiomeFixer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.vault.VaultState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrialChambersFinder extends Finder {

    protected static Map<Direction, List<BlockPos>> SEARCH_POSITIONS;
    protected static final Vec3i size = new Vec3i(19, 20, 19);
    protected List<JigsawFinder> finders = new ArrayList<>();

    public TrialChambersFinder(Level world, ChunkPos chunkPos) {
        super(world, chunkPos);

        Direction.Plane.HORIZONTAL.forEach(direction -> {
            var finder1 = new JigsawFinder(world, chunkPos, direction, size);
            finder1.searchPositions = SEARCH_POSITIONS.get(direction);
            buildEnd1(finder1);
            this.finders.add(finder1);

            var finder2 = new JigsawFinder(world, chunkPos, direction, size);
            finder2.searchPositions = SEARCH_POSITIONS.get(direction);
            buildEnd2(finder2);
            this.finders.add(finder2);
        });
    }

    public static void reloadSearchPositions() {
        SEARCH_POSITIONS = JigsawFinder.getSearchPositions(0, 0,0,0, size);
    }

    public static List<Finder> create(Level world, ChunkPos chunkPos) {
        List<Finder> finders = new ArrayList<>();
        finders.add(new TrialChambersFinder(world, chunkPos));
        return finders;
    }

    public static void buildOminousVault(JigsawFinder finder, int x, int y, int z) {
        var air = Blocks.AIR.defaultBlockState();
        var polishedTuff = Blocks.POLISHED_TUFF.defaultBlockState();
        var polishedTuffSlabTop = Blocks.POLISHED_TUFF_SLAB.defaultBlockState()
                .setValue(SlabBlock.TYPE, SlabType.TOP);
        var ominousVault = Blocks.VAULT.defaultBlockState()
                .setValue(VaultBlock.FACING, Direction.NORTH)
                .setValue(VaultBlock.OMINOUS, true)
                .setValue(VaultBlock.STATE, VaultState.INACTIVE);
        var tuffBricks = Blocks.TUFF_BRICKS.defaultBlockState();
        var chiseledTuff = Blocks.CHISELED_TUFF.defaultBlockState();
        var redGlazedTerracotta = Blocks.RED_GLAZED_TERRACOTTA.defaultBlockState();
        var waxedCopperBulbOn = Blocks.WAXED_COPPER_BULB.defaultBlockState()
                .setValue(CopperBulbBlock.LIT, true);
        var redCandle3 = Blocks.RED_CANDLE.defaultBlockState()
                .setValue(CandleBlock.CANDLES, 3)
                .setValue(CandleBlock.LIT, true);
        var redCandle4 = Blocks.RED_CANDLE.defaultBlockState()
                .setValue(CandleBlock.CANDLES, 4)
                .setValue(CandleBlock.LIT, true);
        var waxedOxidizedCopperGrate = Blocks.WAXED_OXIDIZED_COPPER_GRATE.defaultBlockState();

        finder.fillWithOutline(x, y, z, x + 2, y, z + 1, waxedOxidizedCopperGrate, waxedOxidizedCopperGrate, false);
        finder.addBlock(redCandle3, x, y + 1, z);
        finder.addBlock(redCandle4, x + 2, y + 1, z);
        finder.fillWithOutline(x, y + 1, z + 1, x + 2, y + 3, z + 1, polishedTuff, polishedTuff, false);
        finder.fillWithOutline(x, y + 2, z + 1, x + 2, y + 2, z + 1, polishedTuffSlabTop, polishedTuffSlabTop, false);
        finder.addBlock(ominousVault, x + 1, y + 1, z + 1);
        finder.addBlock(air, x + 1, y + 2, z + 1);
        finder.addBlock(waxedCopperBulbOn, x + 1, y + 3, z + 1);

        finder.fillWithOutline(x, y, z + 2, x + 2, y, z + 2, polishedTuff, polishedTuff, false);
        finder.fillWithOutline(x, y + 1, z + 2, x + 2, y + 3, z + 2, tuffBricks, tuffBricks, false);
        finder.addBlock(waxedCopperBulbOn, x + 1, y + 1, z + 2);
        finder.fillWithOutline(x, y + 2, z + 2, x + 2, y + 2, z + 2, redGlazedTerracotta, redGlazedTerracotta, false);
        finder.addBlock(chiseledTuff, x + 1, y + 2, z + 2);
    }

    public static void buildEnd1(JigsawFinder finder) {
        var air = Blocks.AIR.defaultBlockState();
        var tuffBricks = Blocks.TUFF_BRICKS.defaultBlockState();
        var waxedOxidizedCutCopper = Blocks.WAXED_OXIDIZED_CUT_COPPER.defaultBlockState();
        var waxedCopperBulbOn = Blocks.WAXED_COPPER_BULB.defaultBlockState()
                .setValue(CopperBulbBlock.LIT, true);
        var waxedCopperBlock = Blocks.WAXED_COPPER_BLOCK.defaultBlockState();
        var chiseledTuff = Blocks.CHISELED_TUFF.defaultBlockState();
        var chiseledTuffBricks = Blocks.CHISELED_TUFF_BRICKS.defaultBlockState();
        var polishedTuff = Blocks.POLISHED_TUFF.defaultBlockState();

        // Sides
        finder.fillWithOutline(0, 0, 0, 0, 18, 18, tuffBricks, tuffBricks, false);
        finder.fillWithOutline(18, 0, 0, 18, 18, 18, tuffBricks, tuffBricks, false);

        // Top
        finder.fillWithOutline(0, 19, 0, 18, 19, 18, tuffBricks, tuffBricks, false);

        // Bottom
        finder.fillWithOutline(0, 0, 0, 18, 1, 18, tuffBricks, tuffBricks, false);

        // Wall
        // finder.fillWithOutline(6, 4, 11, 12, 18, 12, tuffBricks, tuffBricks, false);
        finder.fillWithOutline(6, 6, 11, 12, 18, 12, tuffBricks, tuffBricks, false);

        // Base
        finder.fillWithOutline(6, 3, 11, 12, 3, 12, waxedOxidizedCutCopper, waxedOxidizedCutCopper, false);

        // Copper Bulb
        // finder.addBlock(waxedCopperBulbOn, 9, 4, 12);

        // Copper rings
        finder.fillWithOutline(6, 10, 11, 12, 10, 12, waxedCopperBlock, waxedCopperBlock, false);
        finder.fillWithOutline(6, 15, 11, 12, 15, 12, waxedCopperBlock, waxedCopperBlock, false);

        // Chiseled tuff line
        finder.fillWithOutline(6, 5, 12, 12, 5, 12, chiseledTuff, chiseledTuff, false);
        finder.fillWithOutline(6, 5, 11, 7, 5, 12, waxedCopperBlock, waxedCopperBlock, false);
        finder.fillWithOutline(11, 5, 11, 12, 5, 11, waxedCopperBlock, waxedCopperBlock, false);
        finder.fillWithOutline(7, 5, 12, 12, 5, 12, chiseledTuffBricks, chiseledTuffBricks, false);
        finder.addBlock(chiseledTuff, 8, 5, 12);
        finder.addBlock(chiseledTuff, 10, 5, 12);

        // Chiseled tuff brick rings
        finder.fillWithOutline(6, 7, 11, 12, 7, 12, chiseledTuffBricks, chiseledTuffBricks, false);
        finder.fillWithOutline(6, 12, 11, 12, 12, 12, chiseledTuffBricks, chiseledTuffBricks, false);
        finder.fillWithOutline(6, 17, 11, 12, 17, 12, chiseledTuffBricks, chiseledTuffBricks, false);

        // Carve
        finder.fillWithOutline(8, 3, 11, 10, 17, 11, air, air, false);
        finder.fillWithOutline(8, 18, 11, 10, 18, 12, polishedTuff, polishedTuff, false);

        // Vault
        buildOminousVault(finder, 8, 13, 10);
    }

    public static void buildEnd2(JigsawFinder finder) {
        var water = Blocks.WATER.defaultBlockState();
        var tuffBricks = Blocks.TUFF_BRICKS.defaultBlockState();
        var waxedOxidizedCopper = Blocks.WAXED_OXIDIZED_COPPER.defaultBlockState();
        var waxedCopperGrate = Blocks.WAXED_COPPER_GRATE.defaultBlockState();
        var diamondChest = Blocks.CHEST.defaultBlockState()
                .setValue(ChestBlock.FACING, Direction.SOUTH)
                .setValue(ChestBlock.TYPE, ChestType.SINGLE);

        // Sides
        finder.fillWithOutline(0, 0, 0, 0, 18, 18, tuffBricks, tuffBricks, false);
        finder.fillWithOutline(18, 0, 0, 18, 18, 18, tuffBricks, tuffBricks, false);

        // Top
        finder.fillWithOutline(0, 19, 0, 18, 19, 18, tuffBricks, tuffBricks, false);

        // Bottom
        finder.fillWithOutline(0, 0, 0, 18, 0, 18, tuffBricks, tuffBricks, false);
        finder.fillWithOutline(8, 0, 13, 10, 0, 17, waxedOxidizedCopper, waxedOxidizedCopper, false);

        // Water
        finder.fillWithOutline(4, 2, 8, 14, 2, 9, water, water, false);
        finder.fillWithOutline(3, 2, 11, 4, 2, 15, water, water, false);
        finder.fillWithOutline(14, 2, 11, 15, 2, 15, water, water, false);

        // Grates
        finder.fillWithOutline(8, 3, 11, 10, 5, 11, waxedCopperGrate, waxedCopperGrate, false);
        finder.fillWithOutline(7, 3, 13, 7, 5, 16, waxedCopperGrate, waxedCopperGrate, false);
        finder.fillWithOutline(11, 3, 13, 11, 5, 16, waxedCopperGrate, waxedCopperGrate, false);

        // Diamond Chest
        finder.addBlock(diamondChest, 9, 3, 6);

        // Vault
        buildOminousVault(finder, 8, 1, 15);
    }

    @Override
    public List<BlockPos> findInChunk() {
        Biome biome = this.world.getNoiseBiome((this.chunkPos.x << 2) + 2, 64, (this.chunkPos.z << 2) + 2).value();
        // TODO: replace with following once deep dark is implemented
        // if (!Features.TRIAL_CHAMBERS.isValidBiome(BiomeFixer.swap(biome))) return new ArrayList<>();
        if (Minecraft.getInstance().level.registryAccess().lookup(Registries.BIOME).get().getKey(biome).equals(Biomes.DEEP_DARK)) return new ArrayList<>();

        Map<JigsawFinder, List<BlockPos>> result = this.findInChunkPieces();
        List<BlockPos> combinedResult = new ArrayList<>();

        result.forEach((pieceFinder, positions) -> {

            combinedResult.addAll(positions);

            positions.forEach(pos -> {
                RegionStructure.Data<?> data = Features.TRIAL_CHAMBERS.at(this.chunkPos.x, this.chunkPos.z);

                if (SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_STRUCTURES)) {
                    this.cuboids.add(new Cuboid(pos, pieceFinder.getLayout(), ARGB.color(170, 84, 3)));
                    this.cuboids.add(new Cuboid(chunkPos.getWorldPosition().offset(0, pos.getY(), 0), ARGB.color(170, 84, 3)));
                }
            });
        });

        return combinedResult;
    }

    public Map<JigsawFinder, List<BlockPos>> findInChunkPieces() {
        Map<JigsawFinder, List<BlockPos>> result = new HashMap<>();

        this.finders.forEach(pieceFinder -> {
            result.put(pieceFinder, pieceFinder.findInChunk());
        });

        return result;
    }

    @Override
    public boolean isValidDimension(DimensionType dimension) {
        return this.isOverworld(dimension);
    }

}
