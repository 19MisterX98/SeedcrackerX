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

        BURIED_TREASURE = safe(STRUCTURE_TYPES, () -> new BuriedTreasure(version));
        DESERT_PYRAMID = safe(STRUCTURE_TYPES, () -> new DesertPyramid(version));
        END_CITY = safe(STRUCTURE_TYPES, () -> new EndCity(version));
        JUNGLE_PYRAMID = safe(STRUCTURE_TYPES, () -> new JunglePyramid(version));
        MONUMENT = safe(STRUCTURE_TYPES, () -> new Monument(version));
        SHIPWRECK = safe(STRUCTURE_TYPES, () -> new Shipwreck(version));
        SWAMP_HUT = safe(STRUCTURE_TYPES, () -> new SwampHut(version));
        PILLAGER_OUTPOST = safe(STRUCTURE_TYPES, () -> new PillagerOutpost(version));
        IGLOO = safe(STRUCTURE_TYPES, () -> new Igloo(version));

        END_GATEWAY = safe(() -> new EndGateway(version));
        DESERT_WELL = safe(() -> new DesertWell(version));
        EMERALD_ORE = safe(() -> new EmeraldOre(version));
        DUNGEON = safe(() -> new Dungeon(version));
        DEEP_DUNGEON = safe(() -> new DeepDungeon(version));
        WARPED_FUNGUS = safe(() -> new WarpedFungus(version));

        STRUCTURE_TYPES.trimToSize();
    }

    private static <F extends Feature<?, ?>> F safe(Supplier<F> lambda) {
        try {
            return lambda.get();
        } catch (Throwable t) {
            SeedCracker.LOGGER.error("Exception thrown loading feature", t);
            return null;
        }
    }

    private static <F extends RegionStructure<?, ?>> F safe(List<RegionStructure<?, ?>> list, Supplier<F> lambda) {
        F initializedFeature = safe(lambda);
        if (initializedFeature != null) list.add(initializedFeature);
        return initializedFeature;
    }

}
