package kaptainwutax.seedcracker.profile.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import com.google.gson.Gson;


import kaptainwutax.seedcracker.finder.FinderQueue;
import kaptainwutax.seedcracker.finder.FinderQueue.RenderType;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedcracker.Features;
import kaptainwutax.seedcracker.SeedCracker;

import kaptainwutax.seedcracker.finder.Finder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;



public class ConfigScreen {

    private boolean resetToDefault = false;
    private static ConfigObj config = new ConfigObj();
    private static File file = new File(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toFile(),"seedcracker.json");

    public Screen getConfigScreenByCloth(Screen parent) {
        
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(new TranslatableText("key.seedcracker.category"));
        ConfigEntryBuilder eb = builder.entryBuilder();
        // ConfigCategory info = builder.getOrCreateCategory(new TranslatableText("config.category.info"));

        //     info.addEntry(eb.startTextDescription(new LiteralText("some info")).build());
        //     if(SeedCracker.get().getDataStorage().getTimeMachine().worldSeeds != null) {
        //         info.addEntry(eb.startLongList(new LiteralText("longlist"), SeedCracker.get().getDataStorage().getTimeMachine().worldSeeds).build());
        //     } else {
        //         info.addEntry(eb.startTextDescription(new LiteralText("no worldseeds")).build());
        //     }

        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("config.category.general"));

                general.addEntry(eb.startTextDescription(new LiteralText("This settings-page only only sets sets settings for one ingame session. Go to \"Profile\" tab to change settings longterm")).build());

            general.addEntry(eb.startTextDescription(new LiteralText("==============")).build());

                general.addEntry(eb.startTextDescription(new LiteralText("Version(1.8-1.16 Support for old chunks; versions before 1.16 are only tested for dungeon-shortcutting)")).build());
                general.addEntry(eb.startEnumSelector(new LiteralText("Version"), MCVersion.class, SeedCracker.MC_VERSION).setSaveConsumer(val -> {SeedCracker.MC_VERSION = val; Features.init(SeedCracker.MC_VERSION);}).build());

            general.addEntry(eb.startTextDescription(new LiteralText("==============")).build());

                general.addEntry(eb.startTextDescription(new LiteralText("Visuals")).build());
                general.addEntry(eb.startEnumSelector(new LiteralText("rendertype"), RenderType.class,FinderQueue.get().renderType).setSaveConsumer(val -> FinderQueue.get().renderType = val).build());

            general.addEntry(eb.startTextDescription(new LiteralText("==============")).build());

                general.addEntry(eb.startTextDescription(new LiteralText("Finder toggles")).build());
                for(Finder.Type finderType: Finder.Type.values()) {
                    general.addEntry(eb.startBooleanToggle(new LiteralText(String.valueOf(finderType)), FinderQueue.get().finderProfile.getActive(finderType)).setDefaultValue(FinderQueue.get().finderProfile.getActive(finderType)).setSaveConsumer(val -> FinderQueue.get().finderProfile.setActive(finderType, val)).build());
                }

        ConfigCategory config = builder.getOrCreateCategory(new TranslatableText("config.category.profile"));

                config.addEntry(eb.startTextDescription(new LiteralText("the seedcracker will reset to this state after /seed data clear relogging or pressing \"reset to this profile after pressing save & quit\"")).build());
                config.addEntry(eb.startEnumSelector(new LiteralText("Version"), MCVersion.class, ConfigScreen.config.VERSION).setSaveConsumer(val -> ConfigScreen.config.VERSION = val).build());

            config.addEntry(eb.startTextDescription(new LiteralText("==============")).build());
            
                config.addEntry(eb.startTextDescription(new LiteralText("Visuals")).build());
                config.addEntry(eb.startEnumSelector(new LiteralText("rendertype"), RenderType.class, ConfigScreen.config.RENDER).setSaveConsumer(val -> ConfigScreen.config.RENDER = val).build());

            config.addEntry(eb.startTextDescription(new LiteralText("==============")).build());

                config.addEntry(eb.startTextDescription(new LiteralText("Finder toggles")).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("Buried treasure"), ConfigScreen.config.BURIED_TREASURE).setSaveConsumer(val -> ConfigScreen.config.BURIED_TREASURE = val).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("Desert temple"), ConfigScreen.config.DESERT_TEMPLE).setSaveConsumer(val -> ConfigScreen.config.DESERT_TEMPLE = val).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("End city"), ConfigScreen.config.END_CITY).setSaveConsumer(val -> ConfigScreen.config.END_CITY = val).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("Jungle temple"), ConfigScreen.config.JUNGLE_TEMPLE).setSaveConsumer(val -> ConfigScreen.config.JUNGLE_TEMPLE = val).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("Ocean monument"), ConfigScreen.config.MONUMENT).setSaveConsumer(val -> ConfigScreen.config.MONUMENT = val).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("Witch hut"), ConfigScreen.config.SWAMP_HUT).setSaveConsumer(val -> ConfigScreen.config.SWAMP_HUT = val).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("Shipwreck"), ConfigScreen.config.SHIPWRECK).setSaveConsumer(val -> ConfigScreen.config.SHIPWRECK = val).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("End pillars"), ConfigScreen.config.END_PILLARS).setSaveConsumer(val -> ConfigScreen.config.END_PILLARS = val).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("End gateway"), ConfigScreen.config.END_GATEWAY).setSaveConsumer(val -> ConfigScreen.config.END_GATEWAY = val).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("Dungeon"), ConfigScreen.config.DUNGEON).setSaveConsumer(val -> ConfigScreen.config.DUNGEON = val).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("Emerald ore"), ConfigScreen.config.EMERALD_ORE).setSaveConsumer(val -> ConfigScreen.config.EMERALD_ORE = val).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("Desert well"), ConfigScreen.config.DESERT_WELL).setSaveConsumer(val -> ConfigScreen.config.DESERT_WELL = val).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("Biome"), ConfigScreen.config.BIOME).setSaveConsumer(val -> ConfigScreen.config.BIOME = val).build());

            config.addEntry(eb.startTextDescription(new LiteralText("==============")).build());

                config.addEntry(eb.startBooleanToggle(new LiteralText("reset to this profile after pressing save & quit\""), resetToDefault).setSaveConsumer(val -> resetToDefault = val).build());

        builder.setSavingRunnable(() -> {
            saveConfig(file);
            loadConfig();
            if(resetToDefault) {
                FinderQueue.get().clear();
                resetToDefault = false;
            }
        });
        return builder.build();
        
    }

    public static void saveConfig(File file) {
        Gson gson = new Gson();

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void loadConfig() {
        Gson gson = new Gson();

        try (Reader reader = new FileReader(file)) {
            config = gson.fromJson(reader, ConfigObj.class);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static ConfigObj getConfig() {
        return config;
    }
}
