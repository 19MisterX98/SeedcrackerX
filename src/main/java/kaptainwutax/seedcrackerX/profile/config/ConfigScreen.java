package kaptainwutax.seedcrackerX.profile.config;

import com.google.gson.Gson;
import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.HashedSeedData;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.finder.FinderQueue;
import kaptainwutax.seedcrackerX.finder.FinderQueue.RenderType;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ConfigScreen {

    private boolean resetToDefault = false;
    boolean dataClear = false;
    private static ConfigObj config = new ConfigObj();

    private ArrayList<MCVersion> getSupportedVersions() {
        ArrayList<MCVersion> newerVersions = new ArrayList<>();
        for(MCVersion version:MCVersion.values()) {
            if(version.isOlderThan(MCVersion.v1_8))continue;
            newerVersions.add(version);
        }
        return newerVersions;
    }


    private static final File file = new File(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toFile(),"seedcracker.json");

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
                general.addEntry(eb.startBooleanToggle(new LiteralText("Active"), SeedCracker.get().isActive()).setSaveConsumer(val -> SeedCracker.get().setActive(val)).build());
                general.addEntry(eb.startTextDescription(new LiteralText("Version(1.8-1.16 Support for old chunks; versions before 1.16 are only tested for dungeon-shortcutting)")).build());
                general.addEntry(eb.startDropdownMenu(new LiteralText("Version"), DropdownMenuBuilder.TopCellElementBuilder.of(SeedCracker.MC_VERSION,MCVersion::fromString))
                    .setSelections(getSupportedVersions())
                    .setSuggestionMode(false)
                    .setDefaultValue(SeedCracker.MC_VERSION)
                    .setSaveConsumer(val -> {SeedCracker.MC_VERSION = val; Features.init(SeedCracker.MC_VERSION);})
                    .build());

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
                config.addEntry(eb.startBooleanToggle(new LiteralText("Active"), ConfigScreen.config.isActive()).setSaveConsumer(val -> ConfigScreen.config.Active = val).build());
                config.addEntry(eb.startDropdownMenu(new LiteralText("Version"), DropdownMenuBuilder.TopCellElementBuilder.of(ConfigScreen.config.VERSION,MCVersion::fromString))
                    .setSelections(getSupportedVersions())
                    .setSuggestionMode(false)
                    .setDefaultValue(ConfigScreen.config.VERSION)
                    .setSaveConsumer(val -> ConfigScreen.config.VERSION = val)
                    .build());

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
                config.addEntry(eb.startBooleanToggle(new LiteralText("Warped fungus"),ConfigScreen.config.WARPED_FUNGUS).setSaveConsumer(val -> ConfigScreen.config.WARPED_FUNGUS = val).build());
                config.addEntry(eb.startBooleanToggle(new LiteralText("Biome"), ConfigScreen.config.BIOME).setSaveConsumer(val -> ConfigScreen.config.BIOME = val).build());


            config.addEntry(eb.startTextDescription(new LiteralText("==============")).build());

                config.addEntry(eb.startBooleanToggle(new LiteralText("reset to this profile after pressing save & quit\""), resetToDefault).setSaveConsumer(val -> resetToDefault = val).build());
        ConfigCategory info = builder.getOrCreateCategory(new TranslatableText("config.category.info"));
        //Clear data
        info.addEntry(eb.startBooleanToggle(new LiteralText("Clear all data"),dataClear).setSaveConsumer(val -> dataClear = val).build());
        //List worldseeds
        Set<Long> worldSeeds = SeedCracker.get().getDataStorage().getTimeMachine().worldSeeds;
        if(!worldSeeds.isEmpty()) {
            SubCategoryBuilder world = eb.startSubCategory(new LiteralText("Worldseeds"));
            for(long worldSeed:worldSeeds) {
                world.add(eb.startTextField(new LiteralText(""), String.valueOf(worldSeed)).build());
            }
            info.addEntry(world.setExpanded(true).build());
        } else {
            info.addEntry(eb.startTextDescription(new LiteralText("No worldseeds found")).build());
        }
        //List structureseeds
        Set<Long> structureSeeds = SeedCracker.get().getDataStorage().getTimeMachine().structureSeeds;
        if(!structureSeeds.isEmpty()) {
            SubCategoryBuilder struc = eb.startSubCategory(new LiteralText("Structureseeds"));
            for(long structureSeed:structureSeeds) {
                struc.add(eb.startTextField(new LiteralText(""),String.valueOf(structureSeed)).build());
            }
            info.addEntry(struc.setExpanded(true).build());
        } else {
            info.addEntry(eb.startTextDescription(new LiteralText("No structureseeds found")).build());
        }



        if(getConfig().isDEBUG()) {
            //List pillarseeds
            List<Integer> pillarSeeds = SeedCracker.get().getDataStorage().getTimeMachine().pillarSeeds;
            if(pillarSeeds != null) {
                SubCategoryBuilder pillar = eb.startSubCategory(new LiteralText("Pillarseeds"));
                for(long structureSeed:pillarSeeds) {
                    pillar.add(eb.startTextField(new LiteralText(""),String.valueOf(structureSeed)).build());
                }
                info.addEntry(pillar.setExpanded(true).build());
            } else {
                info.addEntry(eb.startTextDescription(new LiteralText("No Pillarseeds found")).build());
            }
            HashedSeedData hashedSeed = SeedCracker.get().getDataStorage().hashedSeedData;
            if(hashedSeed != null) {
                info.addEntry(eb.startTextField(new LiteralText("Hashed seed"),String.valueOf(hashedSeed.getHashedSeed())).build());
            } else {
                info.addEntry(eb.startTextDescription(new LiteralText("no hashed seed found")).build());
            }
        }

        builder.setSavingRunnable(() -> {
            saveConfig(file);
            loadConfig();
            if(dataClear) {
                SeedCracker.get().getDataStorage().clear();
                dataClear = false;
            }
            if(resetToDefault) {
                Features.init(getConfig().VERSION);
                SeedCracker.get().setActive(getConfig().Active);
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
            System.out.println("seedcracker could't save config");
            e.printStackTrace();
        }
    }
    
    public static void loadConfig() {
        Gson gson = new Gson();

        try (Reader reader = new FileReader(file)) {
            config = gson.fromJson(reader, ConfigObj.class);
        } catch (IOException e){
            System.out.println("seedcracker couldn't find/load config");
        }
    }

    public static ConfigObj getConfig() {
        return config;
    }
}
