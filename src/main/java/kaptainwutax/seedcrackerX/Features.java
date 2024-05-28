package kaptainwutax.seedcrackerX;

import com.seedfinding.mccore.version.MCVersion;
import com.seedfinding.mcfeature.Feature;
import com.seedfinding.mcfeature.decorator.DesertWell;
import com.seedfinding.mcfeature.decorator.EndGateway;
import com.seedfinding.mcfeature.structure.*;
import kaptainwutax.seedcrackerX.cracker.decorator.DeepDungeon;
import kaptainwutax.seedcrackerX.cracker.decorator.Dungeon;
import kaptainwutax.seedcrackerX.cracker.decorator.EmeraldOre;
import kaptainwutax.seedcrackerX.cracker.decorator.WarpedFungus;
import kaptainwutax.seedcrackerX.finder.Finder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Features {
    public static final ArrayList<RegionStructure<?, ?>> STRUCTURE_TYPES = new ArrayList<>();

    public static BuriedTreasure BURIED_TREASURE;
    public static DesertPyramid DESERT_PYRAMID;
    public static EndCity END_CITY;
    public static JunglePyramid JUNGLE_PYRAMID;
    public static Monument MONUMENT;
    public static Shipwreck SHIPWRECK;
    public static SwampHut SWAMP_HUT;
    public static PillagerOutpost PILLAGER_OUTPOST;
    public static Igloo IGLOO;

    public static EndGateway END_GATEWAY;
    public static DesertWell DESERT_WELL;
    public static EmeraldOre EMERALD_ORE;
    public static Dungeon DUNGEON;
    public static DeepDungeon DEEP_DUNGEON;
    public static WarpedFungus WARPED_FUNGUS;

    public static void init(MCVersion version) {
        STRUCTURE_TYPES.clear();

        BURIED_TREASURE = safe(STRUCTURE_TYPES, Finder.Type.BURIED_TREASURE, () -> new BuriedTreasure(version));
        DESERT_PYRAMID = safe(STRUCTURE_TYPES, Finder.Type.DESERT_TEMPLE, () -> new DesertPyramid(version));
        END_CITY = safe(STRUCTURE_TYPES, Finder.Type.END_CITY, () -> new EndCity(version));
        JUNGLE_PYRAMID = safe(STRUCTURE_TYPES, Finder.Type.JUNGLE_TEMPLE, () -> new JunglePyramid(version));
        MONUMENT = safe(STRUCTURE_TYPES, Finder.Type.MONUMENT, () -> new Monument(version));
        SHIPWRECK = safe(STRUCTURE_TYPES, Finder.Type.SHIPWRECK, () -> new Shipwreck(version));
        SWAMP_HUT = safe(STRUCTURE_TYPES, Finder.Type.SWAMP_HUT, () -> new SwampHut(version));
        PILLAGER_OUTPOST = safe(STRUCTURE_TYPES, Finder.Type.PILLAGER_OUTPOST, () -> new PillagerOutpost(version));
        IGLOO = safe(STRUCTURE_TYPES, Finder.Type.IGLOO, () -> new Igloo(version));

        END_GATEWAY = safe(Finder.Type.END_GATEWAY, () -> new EndGateway(version));
        DESERT_WELL = safe(Finder.Type.DESERT_WELL, () -> new DesertWell(version));
        EMERALD_ORE = safe(Finder.Type.EMERALD_ORE, () -> new EmeraldOre(version));
        DUNGEON = safe(Finder.Type.DUNGEON, () -> new Dungeon(version));
        DEEP_DUNGEON = safe(Finder.Type.DUNGEON, () -> new DeepDungeon(version));
        WARPED_FUNGUS = safe(Finder.Type.WARPED_FUNGUS, () -> new WarpedFungus(version));

        STRUCTURE_TYPES.trimToSize();
    }

    private static <F extends Feature<?, ?>> F safe(Finder.Type finderType, Supplier<F> lambda) {
        try {
            return lambda.get();
        } catch (Throwable t) {
            SeedCracker.LOGGER.error("Exception thrown loading feature", t);
            finderType.enabled.set(false);
            return null;
        }
    }

    private static <F extends RegionStructure<?, ?>> F safe(List<RegionStructure<?, ?>> list, Finder.Type finderType, Supplier<F> lambda) {
        F initializedFeature = safe(finderType, lambda);
        if (initializedFeature != null) list.add(initializedFeature);
        return initializedFeature;
    }

}
