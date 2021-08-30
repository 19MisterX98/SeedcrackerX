package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kaptainwutax.seedcrackerX.SeedCracker;
import net.minecraft.server.command.ServerCommandSource;

public class GuiCommand extends ClientCommand {

    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        builder.executes(this::openGui);
    }

    private int openGui(CommandContext<ServerCommandSource> context) {
        SeedCracker.get().getDataStorage().openGui = true; //gui needs to open on the main thread
        return 0;
    }
}
