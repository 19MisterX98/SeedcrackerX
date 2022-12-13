package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcrackerX.init.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;


public abstract class ClientCommand {

    public static void sendFeedback(String message, Formatting color, boolean overlay) {
        try {
            MinecraftClient.getInstance().player.sendMessage(Text.literal(message).formatted(color), overlay);
        } catch (Exception e) {
        }
    }

    public abstract String getName();

    public abstract void build(LiteralArgumentBuilder<FabricClientCommandSource> builder);

    public final void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> builder = literal(this.getName());
        this.build(builder);
        dispatcher.register(literal(ClientCommands.PREFIX).then(builder));
    }

}
