package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcrackerX.config.Config;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public class RenderCommand extends ClientCommand {

    @Override
    public String getName() {
        return "render";
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        builder.then(literal("outlines")
                .executes(context -> this.printRenderMode())
        );

        for(Config.RenderType renderType: Config.RenderType.values()) {
            builder.then(literal("outlines")
                    .then(literal(renderType.toString()).executes(context -> this.setRenderMode(renderType)))
            );
        }
    }

    private int printRenderMode() {
        sendFeedback("Current render mode is set to [" + Config.get().render + "].", Formatting.AQUA, false);
        return 0;
    }

    private int setRenderMode(Config.RenderType renderType) {
        Config.get().render = renderType;
        Config.save();
        sendFeedback("Set render mode to [" + Config.get().render + "].", Formatting.AQUA, false);
        return 0;
    }

}
