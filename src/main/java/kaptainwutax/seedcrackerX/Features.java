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

        safe(STRUCTURE_TYPES, () -> BURIED_TREASURE = new BuriedTreasure(version));
        safe(STRUCTURE_TYPES, () -> DESERT_PYRAMID = new DesertPyramid(version));
        safe(STRUCTURE_TYPES, () -> END_CITY = new EndCity(version));
        safe(STRUCTURE_TYPES, () -> JUNGLE_PYRAMID = new JunglePyramid(version));
        safe(STRUCTURE_TYPES, () -> MONUMENT = new Monument(version));
        safe(STRUCTURE_TYPES, () -> SHIPWRECK = new Shipwreck(version));
        safe(STRUCTURE_TYPES, () -> SWAMP_HUT = new SwampHut(version));
        safe(STRUCTURE_TYPES, () -> PILLAGER_OUTPOST = new PillagerOutpost(version));
        safe(STRUCTURE_TYPES, () -> IGLOO = new Igloo(version));

        safe(() -> END_GATEWAY = new EndGateway(version));
        safe(() -> DESERT_WELL = new DesertWell(version));
        safe(() -> EMERALD_ORE = new EmeraldOre(version));
        safe(() -> DUNGEON = new Dungeon(version));
        safe(() -> DEEP_DUNGEON = new DeepDungeon(version));
        safe(() -> WARPED_FUNGUS = new WarpedFungus(version));

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

    private static <F extends Feature<?, ?>> void safe(List<F> list, Supplier<F> lambda) {
        F initializedFeature = safe(lambda);
        if (initializedFeature != null) list.add(initializedFeature);
    }

}
