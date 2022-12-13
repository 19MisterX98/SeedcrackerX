package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kaptainwutax.seedcrackerX.SeedCracker;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class GuiCommand extends ClientCommand {

    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        builder.executes(this::openGui);
    }

    private int openGui(CommandContext<FabricClientCommandSource> context) {
        SeedCracker.get().getDataStorage().openGui = true; //gui needs to open on the main thread
        return 0;
    }
}
