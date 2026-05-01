package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcrackerX.init.ClientCommands;
import kaptainwutax.seedcrackerX.util.Log;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.*;

public abstract class ClientCommand {

    public static void sendFeedback(String message, ChatFormatting color) {
        try {
            Minecraft.getInstance().getChatListener().handleSystemMessage(Component.literal(message).withStyle(color), false);
        } catch (Exception e) {
        }
    }

    public abstract String getName();

    public abstract void build(LiteralArgumentBuilder<FabricClientCommandSource> builder);
    
    public String getUsage() {
        return "/seedcracker " + this.getName();
    }

    public final void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> builder = literal(this.getName());
        this.build(builder);
        if (!builder.getArguments().isEmpty() && builder.getCommand() == null) {
            builder.executes(context -> {
                sendFeedback("Usage: " + this.getUsage(), ChatFormatting.RED);
                return 1;
            });
        }
        LiteralArgumentBuilder<FabricClientCommandSource> seedCrackerRootCommand = literal(ClientCommands.PREFIX)
        .executes(context -> {
            Log.error("Error: please enter a valid seedcracker command");
            return 1;
        });
        dispatcher.register(seedCrackerRootCommand.then(builder));
    }
}
