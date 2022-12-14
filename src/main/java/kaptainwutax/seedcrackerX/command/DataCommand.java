package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.seedfinding.mcfeature.structure.RegionStructure;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.config.StructureSave;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.util.Log;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

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
    }

    public int clear(CommandContext<FabricClientCommandSource> context) {
        SeedCracker.get().reset();

        sendFeedback(Language.getInstance().get("data.clearData"), Formatting.GREEN, false);
        return 0;
    }

    private int printBits(CommandContext<FabricClientCommandSource> context) {
        DataStorage s = SeedCracker.get().getDataStorage();
        String message = Language.getInstance().get("data.collectedBits").formatted((int) s.getBaseBits(), (int) s.getWantedBits());
        String message2 = Language.getInstance().get("data.collectedLiftingBits").formatted((int) s.getLiftingBits(), 40);
        sendFeedback(message, Formatting.GREEN, false);
        sendFeedback(message2, Formatting.GREEN, false);
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

}

