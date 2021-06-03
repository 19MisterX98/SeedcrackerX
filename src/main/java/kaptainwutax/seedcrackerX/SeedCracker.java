package kaptainwutax.seedcrackerX;

import kaptainwutax.seedcrackerX.command.ClientCommand;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.finder.FinderQueue;
import kaptainwutax.seedcrackerX.profile.config.ConfigScreen;
import kaptainwutax.seedutils.mc.MCVersion;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Formatting;

public class SeedCracker implements ModInitializer {

	public static MCVersion MC_VERSION = MCVersion.v1_16_2;

    private static final SeedCracker INSTANCE = new SeedCracker();
    private final DataStorage dataStorage = new DataStorage();
	private static boolean active;

	@Override
	public void onInitialize() {
		ConfigScreen.loadConfig();
		active = ConfigScreen.getConfig().isActive();
		Features.init(MC_VERSION);
	}

	public static SeedCracker get() {
	    return INSTANCE;
    }

	public DataStorage getDataStorage() {
		return this.dataStorage;
	}

	public boolean isActive() {
		return active;
	}

    public void setActive(boolean active) {
		if(SeedCracker.active == active) return;
		SeedCracker.active = active;

	    if(active) {
		    ClientCommand.sendFeedback("SeedCracker is active.", Formatting.GREEN, true);
	    } else {
		    ClientCommand.sendFeedback("SeedCracker is not active.", Formatting.RED, true);
	    }
    }

	public void reset() {
		SeedCracker.get().getDataStorage().clear();
		FinderQueue.get().clear();
	}
}
