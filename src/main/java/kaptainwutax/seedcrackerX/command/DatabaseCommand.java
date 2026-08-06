package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;

public class DatabaseCommand extends ClientCommand {

    public static String databaseURL = "https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing";

    @Override
    public String getName() {
        return "database";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.executes(this::openURL);
    }

    public int openURL(CommandContext<CommandSourceStack> context) {
        Util.getPlatform().openUri(databaseURL);
        return 0;
    }
}
