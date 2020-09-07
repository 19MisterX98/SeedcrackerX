package kaptainwutax.seedcracker;

import kaptainwutax.seedcracker.command.ClientCommand;
import kaptainwutax.seedcracker.cracker.storage.DataStorage;
import kaptainwutax.seedcracker.finder.FinderQueue;
import kaptainwutax.seedcracker.render.RenderQueue;
import kaptainwutax.seedutils.mc.MCVersion;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Formatting;

public class SeedCracker implements ModInitializer {

	public static MCVersion MC_VERSION = MCVersion.v1_16_2;

    private static final SeedCracker INSTANCE = new SeedCracker();
    private final DataStorage dataStorage = new DataStorage();
	private boolean active = true;

	@Override
	public void onInitialize() {
		Features.init(MC_VERSION);
		RenderQueue.get().add("hand", FinderQueue.get()::renderFinders);
	}

	public static SeedCracker get() {
	    return INSTANCE;
    }

	public DataStorage getDataStorage() {
		return this.dataStorage;
	}

	public boolean isActive() {
		return this.active;
	}

    public void setActive(boolean active) {
		this.active = active;

	    if(this.active) {
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
