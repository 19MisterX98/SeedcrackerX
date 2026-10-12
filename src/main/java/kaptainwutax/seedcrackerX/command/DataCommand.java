package kaptainwutax.seedcrackerX.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.seedfinding.mcfeature.structure.RegionStructure;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.config.StructureSave;
import kaptainwutax.seedcrackerX.cracker.BiomeData;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.util.Log;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.*;

public class DataCommand extends ClientCommand {

    @Override
    public String getName() {
        return "data";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        builder.then(literal("clear")
                .executes(this::clear)
        );

        builder.then(literal("bits")
                .executes(this::printBits)
        );

        builder.then(literal("restore")
                .executes(this::restoreData)
        );

        builder.then(literal("export")
                .executes(this::exportData)
        );
    }

    public int clear(CommandContext<FabricClientCommandSource> context) {
        SeedCracker.get().reset();

        sendFeedback(Language.getInstance().getOrDefault("data.clearData"), ChatFormatting.GREEN);
        return 0;
    }

    private int printBits(CommandContext<FabricClientCommandSource> context) {
        DataStorage s = SeedCracker.get().getDataStorage();
        String message = Language.getInstance().getOrDefault("data.collectedBits").formatted((int) s.getBaseBits(), (int) s.getWantedBits());
        String message2 = Language.getInstance().getOrDefault("data.collectedLiftingBits").formatted((int) s.getLiftingBits(), 40);
        sendFeedback(message, ChatFormatting.GREEN);
        sendFeedback(message2, ChatFormatting.GREEN);
        return 0;
    }

    private int restoreData(CommandContext<FabricClientCommandSource> context) {
        var preloaded = StructureSave.loadStructures();
        if (!preloaded.isEmpty()) {
            for (RegionStructure.Data<?> data : preloaded) {
                SeedCracker.get().getDataStorage().addBaseData(data, DataAddedEvent.POKE_LIFTING);
            }
            Log.warn("data.restoreStructures",preloaded.size());
        } else {
            Log.warn("data.restoreFailed");
        }
        return 0;
    }

    private int exportData(CommandContext<FabricClientCommandSource> context) {
        DataStorage storage = SeedCracker.get().getDataStorage();
        JsonObject root = new JsonObject();

        JsonArray baseArray = new JsonArray();
        for (DataStorage.Entry<com.seedfinding.mcfeature.Feature.Data<?>> entry : storage.baseSeedData) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", entry.data.feature.getName());
            obj.addProperty("chunkX", entry.data.chunkX);
            obj.addProperty("chunkZ", entry.data.chunkZ);
            baseArray.add(obj);
        }
        root.add("baseData", baseArray);

        JsonArray biomeArray = new JsonArray();
        for (DataStorage.Entry<BiomeData> entry : storage.getBiomeSeedData()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", entry.data.biome.getName());
            obj.addProperty("x", entry.data.x);
            obj.addProperty("z", entry.data.z);
            biomeArray.add(obj);
        }
        root.add("biomeData", biomeArray);

        if (storage.getPillarData() != null) {
            JsonArray heightsArray = new JsonArray();
            for (Integer height : storage.getPillarData().getHeights()) {
                heightsArray.add(height);
            }
            root.add("pillarData", heightsArray);
        }

        if (storage.hashedSeedData != null) {
            root.addProperty("hashedSeed", storage.hashedSeedData.getHashedSeed());
        }

        try {
            Path configDir = FabricLoader.getInstance().getConfigDir().resolve("SeedCrackerX exported data");
            Files.createDirectories(configDir);

            String filename = "export_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".json";
            Path exportPath = configDir.resolve(filename);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(exportPath.toFile())) {
                gson.toJson(root, writer);
            }

            sendFeedback("Successfully exported data to " + filename, ChatFormatting.GREEN);
        } catch (Exception e) {
            SeedCracker.LOGGER.error("Failed to export SeedCrackerX data", e);
            sendFeedback("Failed to export data. Check the logs.", ChatFormatting.RED);
        }

        return 0;
    }

}

