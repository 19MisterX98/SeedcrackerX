package kaptainwutax.seedcrackerX.profile;

import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.profile.config.ConfigScreen;

public class ModmenuProfile extends CustomProfile {

	public ModmenuProfile() {
		super("19MisterX98", true);
		this.locked = false;
		this.setTypeState(Finder.Type.BURIED_TREASURE,ConfigScreen.getConfig().isBURIED_TREASURE());
		this.setTypeState(Finder.Type.DESERT_TEMPLE,ConfigScreen.getConfig().isDESERT_TEMPLE());
		this.setTypeState(Finder.Type.END_CITY,ConfigScreen.getConfig().isEND_CITY());
		this.setTypeState(Finder.Type.JUNGLE_TEMPLE,ConfigScreen.getConfig().isJUNGLE_TEMPLE());
		this.setTypeState(Finder.Type.MONUMENT,ConfigScreen.getConfig().isMONUMENT());
		this.setTypeState(Finder.Type.SWAMP_HUT,ConfigScreen.getConfig().isSWAMP_HUT());
		this.setTypeState(Finder.Type.SHIPWRECK,ConfigScreen.getConfig().isSHIPWRECK());
		this.setTypeState(Finder.Type.END_PILLARS,ConfigScreen.getConfig().isEND_PILLARS());
		this.setTypeState(Finder.Type.END_GATEWAY,ConfigScreen.getConfig().isEND_GATEWAY());
		this.setTypeState(Finder.Type.DUNGEON,ConfigScreen.getConfig().isDUNGEON());
		this.setTypeState(Finder.Type.EMERALD_ORE,ConfigScreen.getConfig().isEMERALD_ORE());
		this.setTypeState(Finder.Type.DESERT_WELL,ConfigScreen.getConfig().isDESERT_WELL());
		this.setTypeState(Finder.Type.WARPED_FUNGUS,ConfigScreen.getConfig().isWARPED_FUNGUS());
		this.setTypeState(Finder.Type.BIOME,ConfigScreen.getConfig().isBIOME());
		SeedCracker.MC_VERSION = ConfigScreen.getConfig().getVERSION();
		Features.init(SeedCracker.MC_VERSION);
	}
}
