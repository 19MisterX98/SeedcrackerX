package kaptainwutax.seedcrackerX;

import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.seedcrackerX.api.SeedCrackerAPI;
import kaptainwutax.seedcrackerX.command.ClientCommand;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.finder.FinderQueue;
import kaptainwutax.seedcrackerX.profile.config.ConfigScreen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Formatting;

import java.util.ArrayList;

public class SeedCracker implements ModInitializer {

	public static MCVersion MC_VERSION = MCVersion.v1_17;

    private static SeedCracker INSTANCE;
    private final DataStorage dataStorage = new DataStorage();
	private static boolean active;
	public static final ArrayList<SeedCrackerAPI> entrypoints = new ArrayList<>();

	@Override
	public void onInitialize() {
		INSTANCE = this;
		ConfigScreen.loadConfig();
		active = ConfigScreen.getConfig().isActive();
		Features.init(MC_VERSION);
		FabricLoader.getInstance().getEntrypointContainers("seedcrackerx", SeedCrackerAPI.class).forEach(entrypoint ->
				entrypoints.add(entrypoint.getEntrypoint()));
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
