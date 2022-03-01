package kaptainwutax.seedcrackerX.config;

import com.seedfinding.mccore.version.MCVersion;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.HashedSeedData;
import kaptainwutax.seedcrackerX.finder.Finder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ConfigScreen {

    private static final Config config = Config.get();

    private ArrayList<MCVersion> getSupportedVersions() {
        ArrayList<MCVersion> newerVersions = new ArrayList<>();
        for (MCVersion version : MCVersion.values()) {
            if (version.isOlderThan(MCVersion.v1_8)) continue;
            newerVersions.add(version);
        }
        return newerVersions;
    }

    public Screen getConfigScreenByCloth(Screen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableText("title"))
                .setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/blackstone.png"))
                .setTransparentBackground(true);
        ConfigEntryBuilder eb = builder.entryBuilder();

        //=============================CONFIG========================
        ConfigCategory settings = builder.getOrCreateCategory(new TranslatableText("settings"));

        settings.addEntry(eb.startBooleanToggle(new TranslatableText("settings.active"), config.active).setSaveConsumer(val -> config.active = val).build());
        settings.addEntry(eb.startBooleanToggle(new LiteralText("Send 10+ player server seeds to the database"), config.databaseSubmits)
                .setTooltip(new LiteralText("Use /seed database to open it in your browser"))
                .setSaveConsumer(val -> config.databaseSubmits = val).build());
        settings.addEntry(eb.startBooleanToggle(new LiteralText("Scramble username in seed database"), config.anonymusSubmits).setSaveConsumer(val -> config.anonymusSubmits = val).build());
        settings.addEntry(eb.startDropdownMenu(new TranslatableText("settings.version"), DropdownMenuBuilder.TopCellElementBuilder.of(config.getVersion(), MCVersion::fromString))
                .setSelections(getSupportedVersions())
                .setSuggestionMode(false)
                .setDefaultValue(config.getVersion())
                .setSaveConsumer(config::setVersion)
                .build());

        settings.addEntry(eb.startTextDescription(new LiteralText("==============")).build());

        settings.addEntry(eb.startTextDescription(new TranslatableText("settings.visuals")).build());
        settings.addEntry(eb.startEnumSelector(new TranslatableText("settings.outline"), Config.RenderType.class, config.render).setSaveConsumer(val -> config.render = val).build());

        settings.addEntry(eb.startTextDescription(new LiteralText("==============")).build());

        settings.addEntry(eb.startTextDescription((new TranslatableText("settings.finderToggles"))).build());
        for (Finder.Type finder : Finder.Type.values()) {
            settings.addEntry(eb.startBooleanToggle(new TranslatableText(finder.nameKey), finder.enabled.get()).setSaveConsumer(val -> finder.enabled.set(val)).build());
        }

        settings.addEntry(eb.startTextDescription(new LiteralText("==============")).build());

        settings.addEntry(eb.startBooleanToggle(new TranslatableText("settings.antiXrayMode"), config.antiXrayBypass).setSaveConsumer(val -> config.antiXrayBypass = val).build());
        settings.addEntry(eb.startTextDescription(new TranslatableText("settings.antiAntiXrayExplained")).build());

        //=============================INFO========================
        ConfigCategory info = builder.getOrCreateCategory(new TranslatableText("info"));
        //Clear data
        info.addEntry(eb.startBooleanToggle(new TranslatableText("info.clearData"), false).setSaveConsumer(val -> {
            if (val) {
                SeedCracker.get().reset();
            }
        }).build());
        //List worldseeds
        Set<Long> worldSeeds = SeedCracker.get().getDataStorage().getTimeMachine().worldSeeds;
        if (!worldSeeds.isEmpty()) {
            SubCategoryBuilder world = eb.startSubCategory(new TranslatableText("info.worldSeeds"));
            for (long worldSeed : worldSeeds) {
                world.add(eb.startTextField(new LiteralText(""), String.valueOf(worldSeed)).build());
            }
            info.addEntry(world.setExpanded(true).build());
        } else {
            info.addEntry(eb.startTextDescription(new TranslatableText("info.noWorldSeeds")).build());
        }
        //List structureseeds
        Set<Long> structureSeeds = SeedCracker.get().getDataStorage().getTimeMachine().structureSeeds;
        if (!structureSeeds.isEmpty()) {
            SubCategoryBuilder struc = eb.startSubCategory(new TranslatableText("info.structureSeeds"));
            for (long structureSeed : structureSeeds) {
                struc.add(eb.startTextField(new LiteralText(""), String.valueOf(structureSeed)).build());
            }
            info.addEntry(struc.setExpanded(true).build());
        } else {
            info.addEntry(eb.startTextDescription(new TranslatableText("info.noStructureSeeds")).build());
        }

        if (config.debug) {
            //List pillarseeds
            List<Integer> pillarSeeds = SeedCracker.get().getDataStorage().getTimeMachine().pillarSeeds;
            if (pillarSeeds != null) {
                SubCategoryBuilder pillar = eb.startSubCategory(new TranslatableText("info.pillarSeeds"));
                for (long structureSeed : pillarSeeds) {
                    pillar.add(eb.startTextField(new LiteralText(""), String.valueOf(structureSeed)).build());
                }
                info.addEntry(pillar.setExpanded(true).build());
            } else {
                info.addEntry(eb.startTextDescription(new TranslatableText("info.noPillarSeeds")).build());
            }
            //Hashed seed
            HashedSeedData hashedSeed = SeedCracker.get().getDataStorage().hashedSeedData;
            if (hashedSeed != null) {
                info.addEntry(eb.startTextField(new TranslatableText("info.hashedSeed"), String.valueOf(hashedSeed.getHashedSeed())).build());
            } else {
                info.addEntry(eb.startTextDescription(new TranslatableText("info.noHashedSeed")).build());
            }
        }

        builder.setSavingRunnable(Config::save);

        return builder.build();

    }
}
