package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcrackerX.init.ClientCommands;
import kaptainwutax.seedcrackerX.util.Log;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

public abstract class ClientCommand {

    public static void sendFeedback(String message, ChatFormatting color, boolean overlay) {
        try {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(Component.literal(message).withStyle(color), overlay);
            }
        } catch (Exception e) {
        }
    }

    public abstract String getName();

    public abstract void build(LiteralArgumentBuilder<CommandSourceStack> builder);

    public final void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(this.getName());
        this.build(builder);
        LiteralArgumentBuilder<CommandSourceStack> seedCrackerRootCommand = Commands.literal(ClientCommands.PREFIX)
                .executes(context -> {
                    Log.error("Error: please enter a valid seedcracker command");
                    return 1;
                });
        dispatcher.register(seedCrackerRootCommand.then(builder));
    }
}
