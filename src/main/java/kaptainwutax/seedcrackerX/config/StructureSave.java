package kaptainwutax.seedcrackerX.config;

import com.seedfinding.mcfeature.Feature;
import com.seedfinding.mcfeature.structure.RegionStructure;
import com.seedfinding.mcfeature.structure.Structure;
import kaptainwutax.seedcrackerX.Features;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.cracker.storage.ScheduledSet;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.util.WorldSavePath;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StructureSave {
    private static final Logger logger = LoggerFactory.getLogger("structureSave");

    public static final File saveDir = new File(FabricLoader.getInstance().getConfigDir().toFile(), "SeedCrackerX saved structures");
    private static final List<RegionStructure<?,?>> structureTypes = List.of(Features.IGLOO,Features.BURIED_TREASURE,
            Features.PILLAGER_OUTPOST,Features.DESERT_PYRAMID, Features.JUNGLE_PYRAMID, Features.END_CITY,
            Features.MONUMENT, Features.SHIPWRECK, Features.SWAMP_HUT);

    public static void saveStructures(ScheduledSet<DataStorage.Entry<Feature.Data<?>>> baseData) {
        saveDir.mkdirs();
        File saveFile = new File(saveDir, getWorldName());
        try {
            saveFile.createNewFile();
            FileWriter writer = new FileWriter(saveFile);
            for (DataStorage.Entry<Feature.Data<?>> dataEntry : baseData) {
                if (dataEntry.data.feature instanceof Structure structure) {
                    String data = Structure.getName(structure.getClass()) + ";" + dataEntry.data.chunkX +
                            ";" + dataEntry.data.chunkZ +
                            "\n";
                    writer.write(data);
                }
            }
            writer.close();
        } catch (IOException e) {
            logger.error("seedcracker could't save structures", e);
        }
    }

    public static List<RegionStructure.Data<?>> loadStructures() {
        saveDir.mkdirs();
        File saveFile = new File(saveDir, getWorldName());
        List<RegionStructure.Data<?>> result = new ArrayList<>();
        if (!saveFile.exists()) return result;
        try {
            FileInputStream fis = new FileInputStream(saveFile);
            Scanner sc = new Scanner(fis);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] info = line.split(";");
                if (info.length != 3) continue;
                String structureName = info[0];
                for (RegionStructure<?,?> idk : structureTypes) {
                    if (structureName.equals(idk.getName())) {
                        result.add(idk.at(Integer.parseInt(info[1]), Integer.parseInt(info[2])));
                        break;
                    }
                }
            }
        } catch (IOException e) {
            logger.error("seedcracker could't load previous structures", e);
        }
        return result;
    }

    private static String getWorldName() {
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            ClientConnection connection = MinecraftClient.getInstance().getNetworkHandler().getConnection();
            if (connection.isLocal()) {
                String address = MinecraftClient.getInstance().getServer().getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString();
                return address.replace("/","_")+".txt";
            } else {
                return connection.getAddress().toString().replace("/","_")+".txt";
            }
        }
        return "Invalid.txt";
    }
}
