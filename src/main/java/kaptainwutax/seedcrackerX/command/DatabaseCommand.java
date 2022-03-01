package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Util;

public class DatabaseCommand extends ClientCommand {

    private static String databaseURL = "https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing";

    @Override
    public String getName() {
        return "database";
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        builder.executes(this::openURL);
    }

    public int openURL(CommandContext<ServerCommandSource> context) {
        Util.getOperatingSystem().open(databaseURL);
        return 0;
    }
}
