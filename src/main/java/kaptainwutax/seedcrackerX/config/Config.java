package kaptainwutax.seedcrackerX.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.seedfinding.mccore.version.MCVersion;
import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.util.FeatureToggle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger("config");

    private static final File file = new File(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toFile(), "seedcracker.json");
    private static Config INSTANCE = new Config();
    public FeatureToggle buriedTreasure = new FeatureToggle(true);
    public FeatureToggle desertTemple = new FeatureToggle(true);
    public FeatureToggle endCity = new FeatureToggle(true);
    public FeatureToggle jungleTemple = new FeatureToggle(true);
    public FeatureToggle monument = new FeatureToggle(true);
    public FeatureToggle swampHut = new FeatureToggle(true);
    public FeatureToggle shipwreck = new FeatureToggle(true);
    public FeatureToggle outpost = new FeatureToggle(true);
    public FeatureToggle igloo = new FeatureToggle(true);
    public FeatureToggle endPillars = new FeatureToggle(true);
    public FeatureToggle endGateway = new FeatureToggle(false);
    public FeatureToggle dungeon = new FeatureToggle(true);
    public FeatureToggle emeraldOre = new FeatureToggle(false);
    public FeatureToggle desertWell = new FeatureToggle(false);
    public FeatureToggle warpedFungus = new FeatureToggle(false);
    public FeatureToggle biome = new FeatureToggle(false);
    public RenderType render = RenderType.XRAY;
    public boolean active = true;
    public boolean debug = false;
    public boolean antiXrayBypass = true;
    private MCVersion version = MCVersion.latest();
    public boolean databaseSubmits = false;
    public boolean anonymusSubmits = false;

    public static void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // make sure that the config directory exists
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(INSTANCE, writer);
        } catch (IOException e) {
            logger.error("seedcracker couldn't save config", e);
        }
    }

    public static void load() {
        Gson gson = new Gson();

        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            INSTANCE = gson.fromJson(reader, Config.class);
        } catch (Exception e) {
            logger.error("seedcracker couldn't load config, deleting it...", e);
            file.delete();
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    public MCVersion getVersion() {
        return version;
    }

    public void setVersion(MCVersion version) {
        if (this.version == version) return;
        this.version = version;
        Features.init(version);
    }

    public enum RenderType {
        OFF, ON, XRAY
    }
}
