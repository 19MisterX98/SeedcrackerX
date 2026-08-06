package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kaptainwutax.seedcrackerX.SeedCracker;
import net.minecraft.commands.CommandSourceStack;

public class GuiCommand extends ClientCommand {

    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.executes(this::openGui);
    }

    private int openGui(CommandContext<CommandSourceStack> context) {
        if (SeedCracker.get() != null && SeedCracker.get().getDataStorage() != null) {
            SeedCracker.get().getDataStorage().openGui = true;
        }
        return 0;
    }
}
