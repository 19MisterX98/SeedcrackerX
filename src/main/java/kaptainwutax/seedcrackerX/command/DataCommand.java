package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.seedfinding.mcfeature.structure.RegionStructure;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.config.StructureSave;
import kaptainwutax.seedcrackerX.cracker.DataAddedEvent;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.util.Log;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;

import static net.minecraft.server.command.CommandManager.literal;

public class DataCommand extends ClientCommand {

    @Override
    public String getName() {
        return "data";
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
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

    public int clear(CommandContext<ServerCommandSource> context) {
        SeedCracker.get().reset();

        sendFeedback(Language.getInstance().get("data.clearData"), Formatting.GREEN, false);
        return 0;
    }

    private int printBits(CommandContext<ServerCommandSource> context) {
        DataStorage s = SeedCracker.get().getDataStorage();
        String message = Language.getInstance().get("data.collectedBits").formatted((int) s.getBaseBits(), (int) s.getWantedBits());
        String message2 = Language.getInstance().get("data.collectedLiftingBits").formatted((int) s.getLiftingBits(), 40);
        sendFeedback(message, Formatting.GREEN, false);
        sendFeedback(message2, Formatting.GREEN, false);
        return 0;
    }

    private int restoreData(CommandContext<ServerCommandSource> context) {
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

