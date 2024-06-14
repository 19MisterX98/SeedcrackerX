package kaptainwutax.seedcrackerX.config;

import com.seedfinding.mccore.version.MCVersion;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.command.DatabaseCommand;
import kaptainwutax.seedcrackerX.cracker.HashedSeedData;
import kaptainwutax.seedcrackerX.finder.Finder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
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

    private MCVersion mcVersionFromString(String version) {
        MCVersion mcVersion = MCVersion.fromString(version);
        if (mcVersion == null) return MCVersion.latest();
        return mcVersion;
    }

    public Screen getConfigScreenByCloth(Screen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title"))
                .setDefaultBackgroundTexture(Identifier.of("minecraft:textures/block/blackstone.png"))
                .setTransparentBackground(true);
        ConfigEntryBuilder eb = builder.entryBuilder();

        //=============================CONFIG========================
        ConfigCategory settings = builder.getOrCreateCategory(Text.translatable("settings"));

        settings.addEntry(eb.startBooleanToggle(Text.translatable("settings.active"), config.active).setSaveConsumer(val -> config.active = val).build());
        settings.addEntry(eb.startBooleanToggle(Text.translatable("settings.database"), config.databaseSubmits)
                .setSaveConsumer(val -> config.databaseSubmits = val).build());
        settings.addEntry(eb.startBooleanToggle(Text.translatable("settings.hideNameDatabase"), config.anonymusSubmits).setSaveConsumer(val -> config.anonymusSubmits = val).build());
        settings.addEntry(eb.startTextDescription(Text.translatable("settings.openDatabase").styled(s -> s
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, DatabaseCommand.databaseURL))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("google sheet")))
                .withColor(Formatting.BLUE)
                .withUnderline(true)
                .withItalic(true)))
                .build());
        settings.addEntry(eb.startDropdownMenu(Text.translatable("settings.version"), DropdownMenuBuilder.TopCellElementBuilder.of(config.getVersion(), this::mcVersionFromString))
                .setSelections(getSupportedVersions())
                .setSuggestionMode(false)
                .setDefaultValue(config.getVersion())
                .setSaveConsumer(config::setVersion)
                .build());

        settings.addEntry(eb.startTextDescription(Text.literal("==============")).build());

        settings.addEntry(eb.startTextDescription(Text.translatable("settings.visuals")).build());
        settings.addEntry(eb.startEnumSelector(Text.translatable("settings.outline"), Config.RenderType.class, config.render).setSaveConsumer(val -> config.render = val).build());

        settings.addEntry(eb.startTextDescription(Text.literal("==============")).build());

        settings.addEntry(eb.startTextDescription((Text.translatable("settings.finderToggles"))).build());
        for (Finder.Type finder : Finder.Type.values()) {
            settings.addEntry(eb.startBooleanToggle(Text.translatable(finder.nameKey), finder.enabled.get()).setSaveConsumer(val -> finder.enabled.set(val)).build());
        }

        settings.addEntry(eb.startTextDescription(Text.literal("==============")).build());

        settings.addEntry(eb.startBooleanToggle(Text.translatable("settings.antiXrayMode"), config.antiXrayBypass).setSaveConsumer(val -> config.antiXrayBypass = val).build());
        settings.addEntry(eb.startTextDescription(Text.translatable("settings.antiAntiXrayExplained")).build());

        //=============================INFO========================
        ConfigCategory info = builder.getOrCreateCategory(Text.translatable("info"));
        //Clear data
        info.addEntry(eb.startBooleanToggle(Text.translatable("info.clearData"), false).setSaveConsumer(val -> {
            if (val) {
                SeedCracker.get().reset();
            }
        }).build());
        //List worldseeds
        Set<Long> worldSeeds = SeedCracker.get().getDataStorage().getTimeMachine().worldSeeds;
        if (!worldSeeds.isEmpty()) {
            SubCategoryBuilder world = eb.startSubCategory(Text.translatable("info.worldSeeds"));
            for (long worldSeed : worldSeeds) {
                world.add(eb.startTextField(Text.literal(""), String.valueOf(worldSeed)).build());
            }
            info.addEntry(world.setExpanded(true).build());
        } else {
            info.addEntry(eb.startTextDescription(Text.translatable("info.noWorldSeeds")).build());
        }
        //List structureseeds
        Set<Long> structureSeeds = SeedCracker.get().getDataStorage().getTimeMachine().structureSeeds;
        if (!structureSeeds.isEmpty()) {
            SubCategoryBuilder struc = eb.startSubCategory(Text.translatable("info.structureSeeds"));
            for (long structureSeed : structureSeeds) {
                struc.add(eb.startTextField(Text.literal(""), String.valueOf(structureSeed)).build());
            }
            info.addEntry(struc.setExpanded(true).build());
        } else {
            info.addEntry(eb.startTextDescription(Text.translatable("info.noStructureSeeds")).build());
        }

        if (config.debug) {
            //List pillarseeds
            List<Integer> pillarSeeds = SeedCracker.get().getDataStorage().getTimeMachine().pillarSeeds;
            if (pillarSeeds != null) {
                SubCategoryBuilder pillar = eb.startSubCategory(Text.translatable("info.pillarSeeds"));
                for (long structureSeed : pillarSeeds) {
                    pillar.add(eb.startTextField(Text.literal(""), String.valueOf(structureSeed)).build());
                }
                info.addEntry(pillar.setExpanded(true).build());
            } else {
                info.addEntry(eb.startTextDescription(Text.translatable("info.noPillarSeeds")).build());
            }
            //Hashed seed
            HashedSeedData hashedSeed = SeedCracker.get().getDataStorage().hashedSeedData;
            if (hashedSeed != null) {
                info.addEntry(eb.startTextField(Text.translatable("info.hashedSeed"), String.valueOf(hashedSeed.getHashedSeed())).build());
            } else {
                info.addEntry(eb.startTextDescription(Text.translatable("info.noHashedSeed")).build());
            }
        }

        builder.setSavingRunnable(Config::save);

        return builder.build();

    }
}
