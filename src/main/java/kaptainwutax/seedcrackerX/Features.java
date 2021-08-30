package kaptainwutax.seedcrackerX;

import kaptainwutax.featureutils.decorator.DesertWell;
import kaptainwutax.featureutils.decorator.EndGateway;
import kaptainwutax.featureutils.structure.*;
import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.seedcrackerX.cracker.decorator.Dungeon;
import kaptainwutax.seedcrackerX.cracker.decorator.EmeraldOre;
import kaptainwutax.seedcrackerX.cracker.decorator.WarpedFungus;

public class Features {

	public static BuriedTreasure BURIED_TREASURE;
	public static DesertPyramid DESERT_PYRAMID;
	public static EndCity END_CITY;
	public static JunglePyramid JUNGLE_PYRAMID;
	public static Monument MONUMENT;
	public static Shipwreck SHIPWRECK;
	public static SwampHut SWAMP_HUT;

	public static EndGateway END_GATEWAY;
	public static DesertWell DESERT_WELL;
	public static EmeraldOre EMERALD_ORE;
	public static Dungeon DUNGEON;
	public static WarpedFungus WARPED_FUNGUS;
	
	public static void init(MCVersion version) {
		safe(() -> BURIED_TREASURE = new BuriedTreasure(version));
		safe(() -> DESERT_PYRAMID = new DesertPyramid(version));
		safe(() -> END_CITY = new EndCity(version));
		safe(() -> JUNGLE_PYRAMID = new JunglePyramid(version));
		safe(() -> MONUMENT = new Monument(version));
		safe(() -> SHIPWRECK = new Shipwreck(version));
		safe(() -> SWAMP_HUT = new SwampHut(version));

		safe(() -> END_GATEWAY = new EndGateway(version));
		safe(() -> DESERT_WELL = new DesertWell(version));
		safe(() -> EMERALD_ORE = new EmeraldOre(version));
		safe(() -> DUNGEON = new Dungeon(version));
		safe(() -> WARPED_FUNGUS = new WarpedFungus(version));
	}

	private static void safe(Runnable runnable) {
		try {runnable.run();} catch(Exception e) {}
	}

}
