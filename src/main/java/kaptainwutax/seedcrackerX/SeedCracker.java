package kaptainwutax.seedcrackerX;

import com.mojang.logging.LogUtils;
import kaptainwutax.seedcrackerX.api.SeedCrackerAPI;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.finder.FinderQueue;
import kaptainwutax.seedcrackerX.init.ClientCommands;
import kaptainwutax.seedcrackerX.util.Database;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.util.ArrayList;

/**
* SeedCrackerX is a comprehensive Minecraft mod initialization class that serves as the primary entry point 
* for the seed cracking tool. As a ModInitializer, it sets up the entire mod's infrastructure by initializing critical 
* components like configuration, data storage, and client commands. The class leverages the Fabric modding framework to 
* register client-side commands and provides a centralized setup for seed manipulation features. It integrates 
* multiple subsystems including the SeedCrackerAPI for external interactions, ClientCommands for user interface, DataStorage for 
* managing seed-related data, and Database for potential online seed tracking. The mod's architecture is designed to be 
* flexible and version-adaptable, enabling advanced exploration of Minecraft world generation mechanics through a singleton-based 
* approach that coordinates various seed cracking functionalities. By bridging configuration, command registration, and data management, 
* this class acts as the core orchestrator for the SeedCrackerX mod's complex seed analysis capabilities.
*/

public class SeedCracker implements ModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ArrayList<SeedCrackerAPI> entrypoints = new ArrayList<>();
    private static SeedCracker INSTANCE;
    private final DataStorage dataStorage = new DataStorage();

    public static SeedCracker get() {
        return INSTANCE;
    }

    @Override
    public void onInitialize() {
        INSTANCE = this;
        Config.load();
        Features.init(Config.get().getVersion());
        FabricLoader.getInstance().getEntrypointContainers("seedcrackerx", SeedCrackerAPI.class).forEach(entrypoint ->
                entrypoints.add(entrypoint.getEntrypoint()));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> ClientCommands.registerCommands(dispatcher));

        Database.fetchSeeds();
    }

    public DataStorage getDataStorage() {
        return this.dataStorage;
    }

    public void reset() {
        SeedCracker.get().getDataStorage().clear();
        FinderQueue.get().finderControl.deleteFinders();
    }
}
