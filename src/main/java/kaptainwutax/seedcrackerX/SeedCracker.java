package kaptainwutax.seedcrackerX;

import kaptainwutax.seedcrackerX.api.SeedCrackerAPI;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.finder.FinderQueue;
import kaptainwutax.seedcrackerX.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;

public class SeedCracker implements ModInitializer {

    private static SeedCracker INSTANCE;
    private final DataStorage dataStorage = new DataStorage();
	public static final ArrayList<SeedCrackerAPI> entrypoints = new ArrayList<>();

	@Override
	public void onInitialize() {
		INSTANCE = this;
		Config.load();
		Features.init(Config.get().getVersion());
		FabricLoader.getInstance().getEntrypointContainers("seedcrackerx", SeedCrackerAPI.class).forEach(entrypoint ->
				entrypoints.add(entrypoint.getEntrypoint()));
	}

	public static SeedCracker get() {
	    return INSTANCE;
    }

	public DataStorage getDataStorage() {
		return this.dataStorage;
	}

	public void reset() {
		SeedCracker.get().getDataStorage().clear();
		FinderQueue.get().finderControl.deleteFinders();
	}
}
