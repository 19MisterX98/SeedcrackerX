package kaptainwutax.seedcrackerX.config;

import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.HashedSeedData;
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
        for(MCVersion version:MCVersion.values()) {
            if(version.isOlderThan(MCVersion.v1_8))continue;
            newerVersions.add(version);
        }
        return newerVersions;
    }

    public Screen getConfigScreenByCloth(Screen parent) {
        
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(new TranslatableText("seedcrackerx.title"))
            .setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/blackstone.png"))
            .setTransparentBackground(true);
        ConfigEntryBuilder eb = builder.entryBuilder();

        //=============================CONFIG========================
        ConfigCategory settings = builder.getOrCreateCategory(new TranslatableText("seedcrackerx.settings"));
        
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.active"), config.active).setSaveConsumer(val -> config.active = val).build());
        settings.addEntry(eb.startDropdownMenu(new TranslatableText("seedcrackerx.settings.version"), DropdownMenuBuilder.TopCellElementBuilder.of(config.getVersion(), MCVersion::fromString))
            .setSelections(getSupportedVersions())
            .setSuggestionMode(false)
            .setDefaultValue(config.getVersion())
            .setSaveConsumer(config::setVersion)
            .build());

        settings.addEntry(eb.startTextDescription(new LiteralText("==============")).build());
        
        settings.addEntry(eb.startTextDescription(new TranslatableText("seedcrackerx.settings.visuals")).build());
        settings.addEntry(eb.startEnumSelector(new TranslatableText("seedcrackerx.settings.outline"), Config.RenderType.class, config.render).setSaveConsumer(val -> config.render = val).build());

        settings.addEntry(eb.startTextDescription(new LiteralText("==============")).build());

        settings.addEntry(eb.startTextDescription((new TranslatableText("seedcrackerx.settings.finderToggles"))).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.buriedTreasures"), config.buriedTreasure.get()).setSaveConsumer(val -> config.buriedTreasure.set(val)).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.desertTemples"), config.desertTemple.get()).setSaveConsumer(val -> config.desertTemple.set(val)).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.endCities"), config.endCity.get()).setSaveConsumer(val -> config.endCity.set(val)).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.jungleTemples"), config.jungleTemple.get()).setSaveConsumer(val -> config.jungleTemple.set(val)).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.monuments"), config.monument.get()).setSaveConsumer(val -> config.monument.set(val)).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.swampHuts"), config.swampHut.get()).setSaveConsumer(val -> config.swampHut.set(val)).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.shipwrecks"), config.shipwreck.get()).setSaveConsumer(val -> config.shipwreck.set(val)).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.endPillars"), config.endPillars.get()).setSaveConsumer(val -> config.endPillars.set(val)).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.endGateways"), config.endGateway.get()).setSaveConsumer(val -> config.endGateway.set(val)).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.dungeons"), config.dungeon.get()).setSaveConsumer(val -> config.dungeon.set(val)).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.emeraldOres"), config.emeraldOre.get()).setSaveConsumer(val -> config.emeraldOre.set(val)).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.desertWells"), config.desertWell.get()).setSaveConsumer(val -> config.desertWell.set(val)).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.warpedFungus"),config.warpedFungus.get()).setSaveConsumer(val -> config.warpedFungus.set(val)).build());
        settings.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.settings.biomes"), config.biome.get()).setSaveConsumer(val -> config.biome.set(val)).build());

        //=============================INFO========================
        ConfigCategory info = builder.getOrCreateCategory(new TranslatableText("seedcrackerx.info"));
        //Clear data
        info.addEntry(eb.startBooleanToggle(new TranslatableText("seedcrackerx.info.clearData"), false).setSaveConsumer(val -> {
            if (val) {
                SeedCracker.get().reset();
            }
        }).build());
        //List worldseeds
        Set<Long> worldSeeds = SeedCracker.get().getDataStorage().getTimeMachine().worldSeeds;
        if(!worldSeeds.isEmpty()) {
            SubCategoryBuilder world = eb.startSubCategory(new TranslatableText("seedcrackerx.info.worldSeeds"));
            for(long worldSeed:worldSeeds) {
                world.add(eb.startTextField(new LiteralText(""), String.valueOf(worldSeed)).build());
            }
            info.addEntry(world.setExpanded(true).build());
        } else {
            info.addEntry(eb.startTextDescription(new TranslatableText("seedcrackerx.info.noWorldSeeds")).build());
        }
        //List structureseeds
        Set<Long> structureSeeds = SeedCracker.get().getDataStorage().getTimeMachine().structureSeeds;
        if(!structureSeeds.isEmpty()) {
            SubCategoryBuilder struc = eb.startSubCategory(new TranslatableText("seedcrackerx.info.structureSeeds"));
            for(long structureSeed:structureSeeds) {
                struc.add(eb.startTextField(new LiteralText(""),String.valueOf(structureSeed)).build());
            }
            info.addEntry(struc.setExpanded(true).build());
        } else {
            info.addEntry(eb.startTextDescription(new TranslatableText("seedcrackerx.info.noStructureSeeds")).build());
        }

        if(config.debug) {
            //List pillarseeds
            List<Integer> pillarSeeds = SeedCracker.get().getDataStorage().getTimeMachine().pillarSeeds;
            if(pillarSeeds != null) {
                SubCategoryBuilder pillar = eb.startSubCategory(new TranslatableText("seedcrackerx.info.pillarSeeds"));
                for(long structureSeed:pillarSeeds) {
                    pillar.add(eb.startTextField(new LiteralText(""),String.valueOf(structureSeed)).build());
                }
                info.addEntry(pillar.setExpanded(true).build());
            } else {
                info.addEntry(eb.startTextDescription(new TranslatableText("seedcrackerx.info.noPillarSeeds")).build());
            }
            //Hashed seed
            HashedSeedData hashedSeed = SeedCracker.get().getDataStorage().hashedSeedData;
            if(hashedSeed != null) {
                info.addEntry(eb.startTextField(new TranslatableText("seedcrackerx.info.hashedSeed"),String.valueOf(hashedSeed.getHashedSeed())).build());
            } else {
                info.addEntry(eb.startTextDescription(new TranslatableText("seedcrackerx.info.noHashedSeed")).build());
            }
        }

        builder.setSavingRunnable(Config::save);

        return builder.build();
        
    }
}
