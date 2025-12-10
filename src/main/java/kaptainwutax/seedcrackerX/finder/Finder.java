package kaptainwutax.seedcrackerX.finder;

import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.finder.decorator.DesertWellFinder;
import kaptainwutax.seedcrackerX.finder.decorator.DungeonFinder;
import kaptainwutax.seedcrackerX.finder.decorator.EndGatewayFinder;
import kaptainwutax.seedcrackerX.finder.decorator.EndPillarsFinder;
import kaptainwutax.seedcrackerX.finder.decorator.WarpedFungusFinder;
import kaptainwutax.seedcrackerX.finder.decorator.ore.EmeraldOreFinder;
import kaptainwutax.seedcrackerX.finder.structure.*;
import kaptainwutax.seedcrackerX.render.Cuboid;
import kaptainwutax.seedcrackerX.util.FeatureToggle;
import kaptainwutax.seedcrackerX.util.HeightContext;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Finder {

    protected static final List<BlockPos> CHUNK_POSITIONS = new ArrayList<>();
    protected static final List<BlockPos> SUB_CHUNK_POSITIONS = new ArrayList<>();
    protected static HeightContext heightContext;

    static {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 16; y++) {
                    SUB_CHUNK_POSITIONS.add(new BlockPos(x, y, z));
                }
            }
        }
    }

    protected Minecraft mc = Minecraft.getInstance();
    protected final List<Cuboid> cuboids = new ArrayList<>();
    protected Level world;
    protected ChunkPos chunkPos;

    public Finder(Level world, ChunkPos chunkPos) {
        this.world = world;
        this.chunkPos = chunkPos;
    }

    public static List<BlockPos> buildSearchPositions(List<BlockPos> base, Predicate<BlockPos> removeIf) {
        List<BlockPos> newList = new ArrayList<>();

        for (BlockPos pos : base) {
            if (!removeIf.test(pos)) {
                newList.add(pos);
            }
        }

        return newList;
    }

    public Level getWorld() {
        return this.world;
    }

    public ChunkPos getChunkPos() {
        return this.chunkPos;
    }

    public abstract List<BlockPos> findInChunk();

    public boolean shouldRender() {
        DimensionType finderDim = this.world.dimensionType();
        DimensionType playerDim = mc.player.level().dimensionType();

        if (finderDim != playerDim) return false;

        int renderDistance = mc.options.renderDistance().get() * 16 + 16;
        Vec3 playerPos = mc.player.position();

        for (Cuboid cuboid : this.cuboids) {
            BlockPos pos = cuboid.getCenterPos();
            double distance = playerPos.distanceToSqr(pos.getX(), playerPos.y, pos.getZ());
            if (distance <= renderDistance * renderDistance + 32) return true;
        }

        return false;
    }

    public boolean isUseless() {
        return this.cuboids.isEmpty();
    }

    public abstract boolean isValidDimension(DimensionType dimension);

    public boolean isOverworld(DimensionType dimension) {
        return dimension.skybox() == DimensionType.Skybox.OVERWORLD;
    }

    public boolean isNether(DimensionType dimension) {
        return dimension.skybox() == DimensionType.Skybox.NONE;
    }

    public boolean isEnd(DimensionType dimension) {
        return dimension.skybox() == DimensionType.Skybox.END;
    }

    public static String inferDimension(DimensionType dimension) {
        return switch (dimension.skybox()) {
            case OVERWORLD -> "overworld";
            case NONE -> "the_nether";
            case END -> "the_end";
        };
    }

    public enum Category {
        STRUCTURES,
        DECORATORS,
        BIOMES,
    }

    public enum Type {
        BURIED_TREASURE(BuriedTreasureFinder::create, Category.STRUCTURES, Config.get().buriedTreasure, "finder.buriedTreasures"),
        DESERT_TEMPLE(DesertPyramidFinder::create, Category.STRUCTURES, Config.get().desertTemple, "finder.desertTemples"),
        END_CITY(EndCityFinder::create, Category.STRUCTURES, Config.get().endCity, "finder.endCities"),
        JUNGLE_TEMPLE(JunglePyramidFinder::create, Category.STRUCTURES, Config.get().jungleTemple, "finder.jungleTemples"),
        MONUMENT(MonumentFinder::create, Category.STRUCTURES, Config.get().monument, "finder.monuments"),
        SWAMP_HUT(SwampHutFinder::create, Category.STRUCTURES, Config.get().swampHut, "finder.swampHuts"),
        SHIPWRECK(ShipwreckFinder::create, Category.STRUCTURES, Config.get().shipwreck, "finder.shipwrecks"),
        PILLAGER_OUTPOST(OutpostFinder::create, Category.STRUCTURES, Config.get().outpost, "finder.outposts"),
        IGLOO(IglooFinder::create, Category.STRUCTURES, Config.get().igloo, "finder.igloo"),
        TRIAL_CHAMBERS(TrialChambersFinder::create, Category.STRUCTURES, Config.get().trialChambers, "finder.trialChambers"),

        END_PILLARS(EndPillarsFinder::create, Category.DECORATORS, Config.get().endPillars, "finder.endPillars"),
        END_GATEWAY(EndGatewayFinder::create, Category.DECORATORS, Config.get().endGateway, "finder.endGateways"),
        DUNGEON(DungeonFinder::create, Category.DECORATORS, Config.get().dungeon, "finder.dungeons"),
        EMERALD_ORE(EmeraldOreFinder::create, Category.DECORATORS, Config.get().emeraldOre, "finder.emeraldOres"),
        DESERT_WELL(DesertWellFinder::create, Category.DECORATORS, Config.get().desertWell, "finder.desertWells"),
        WARPED_FUNGUS(WarpedFungusFinder::create, Category.DECORATORS, Config.get().warpedFungus, "finder.warpedFungus"),

        BIOME(BiomeFinder::create, Category.BIOMES, Config.get().biome, "finder.biomes");

        public final FinderBuilder finderBuilder;
        public final String nameKey;
        private final Category category;
        public FeatureToggle enabled;

        Type(FinderBuilder finderBuilder, Category category, FeatureToggle enabled, String nameKey) {
            this.finderBuilder = finderBuilder;
            this.category = category;
            this.enabled = enabled;
            this.nameKey = nameKey;
        }

        public static List<Type> getForCategory(Category category) {
            return Arrays.stream(values()).filter(type -> type.category == category).collect(Collectors.toList());
        }
    }
}
