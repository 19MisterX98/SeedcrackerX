package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.util.Util;

public class DatabaseCommand extends ClientCommand {

    public static String databaseURL = "https://docs.google.com/spreadsheets/d/1tuQiE-0leW88em9OHbZnH-RFNhVqgoHhIt9WQbeqqWw/edit?usp=sharing";

    @Override
    public String getName() {
        return "database";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        builder.executes(this::openURL);
    }

    public int openURL(CommandContext<FabricClientCommandSource> context) {
        Util.getOperatingSystem().open(databaseURL);
        return 0;
    }
}
