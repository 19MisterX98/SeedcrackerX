package kaptainwutax.seedcrackerX;

import com.mojang.logging.LogUtils;
import kaptainwutax.seedcrackerX.api.SeedCrackerAPI;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.config.ConfigScreen;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.finder.FinderQueue;
import kaptainwutax.seedcrackerX.init.ClientCommands;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import java.util.ArrayList;

@Mod("seedcrackerx")
public class SeedCracker {
    public static final String MOD_ID = "seedcrackerx";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ArrayList<SeedCrackerAPI> entrypoints = new ArrayList<>();
    private static SeedCracker INSTANCE;
    private final DataStorage dataStorage = new DataStorage();

    public SeedCracker(ModContainer modContainer, IEventBus modEventBus) {
        INSTANCE = this;
        Config.load();
        Features.init(Config.get().getVersion());

        modContainer.registerExtensionPoint(IConfigScreenFactory.class,
                (container, parent) -> ConfigScreen.getConfigScreenByCloth(parent));

        modEventBus.addListener(this::onClientSetup);
        NeoForge.EVENT_BUS.register(new ClientCommands());
        NeoForge.EVENT_BUS.register(this);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent.Post event) {
        if (dataStorage != null) {
            dataStorage.tick();
        }
    }

    @SubscribeEvent
    public void onRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            FinderQueue.get().renderFinders(event.getPoseStack().last().pose(), event.getCamera());
        }
    }

    @SubscribeEvent
    public void onLoggingIn(net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingIn event) {
        if (event.getPlayer() != null && event.getPlayer().level() != null) {
            net.minecraft.client.multiplayer.ClientLevel level = (net.minecraft.client.multiplayer.ClientLevel) event.getPlayer().level();
            net.minecraft.world.level.dimension.DimensionType dimension = level.dimensionType();
            kaptainwutax.seedcrackerX.finder.ReloadFinders.reloadHeight(dimension.minY(), dimension.minY() + dimension.logicalHeight());
        }

        var preloaded = kaptainwutax.seedcrackerX.config.StructureSave.loadStructures();
        if (!preloaded.isEmpty()) {
            kaptainwutax.seedcrackerX.util.Log.warn("foundRestorableStructures", preloaded.size());
        }
    }

    @SubscribeEvent
    public void onLoggingOut(net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut event) {
        kaptainwutax.seedcrackerX.config.StructureSave.saveStructures(this.getDataStorage().baseSeedData);
        this.reset();
    }

    @SubscribeEvent
    public void onChunkLoad(net.neoforged.neoforge.event.level.ChunkEvent.Load event) {
        if (event.getLevel() instanceof net.minecraft.world.level.Level level && event.getChunk() != null && level.isClientSide()) {
            FinderQueue.get().onChunkData(level, event.getChunk().getPos());
        }
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
