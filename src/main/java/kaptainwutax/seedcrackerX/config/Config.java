package kaptainwutax.seedcrackerX.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.util.FeatureToggle;

import java.io.*;

public class Config {
    private static Config INSTANCE = new Config();
    private static final File file = new File(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toFile(),"seedcracker.json");
    public FeatureToggle buriedTreasure = new FeatureToggle(true);
    public FeatureToggle desertTemple = new FeatureToggle(true);
    public FeatureToggle endCity = new FeatureToggle(true);
    public FeatureToggle jungleTemple = new FeatureToggle(true);
    public FeatureToggle monument = new FeatureToggle(true);
    public FeatureToggle swampHut = new FeatureToggle(true);
    public FeatureToggle shipwreck = new FeatureToggle(true);
    public FeatureToggle endPillars = new FeatureToggle(true);
    public FeatureToggle endGateway = new FeatureToggle(true);
    public FeatureToggle dungeon = new FeatureToggle(true);
    public FeatureToggle emeraldOre = new FeatureToggle(true);
    public FeatureToggle desertWell = new FeatureToggle(true);
    public FeatureToggle warpedFungus = new FeatureToggle(true);
    public FeatureToggle biome = new FeatureToggle(true);
    private MCVersion version = MCVersion.v1_17_1;
    public RenderType render = RenderType.XRAY;
    public boolean active = true;
    public boolean debug = false;
    public boolean antiXrayBypass = true;

    public MCVersion getVersion() {
        return version;
    }

    public void setVersion(MCVersion version) {
        if (this.version == version) return;
        this.version = version;
        Features.init(version);
    }

    public static void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(file)) {

            gson.toJson(INSTANCE, writer);
        } catch (IOException e) {
            System.out.println("seedcracker could't save config");
            e.printStackTrace();
        }
    }

    public static void load() {
        Gson gson = new Gson();

        try (Reader reader = new FileReader(file)) {
            INSTANCE = gson.fromJson(reader, Config.class);
        } catch (Exception e){
            if (file.exists()) {
                System.out.println("seedcracker couldn't load config, deleting it...");
                file.delete();
            } else {
                System.out.println("seedcracker couldn't find config");
            }
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    public enum RenderType {
        OFF, ON, XRAY
    }
}
