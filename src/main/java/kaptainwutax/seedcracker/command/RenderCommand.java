package kaptainwutax.seedcracker.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcracker.finder.FinderQueue;
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

        for(FinderQueue.RenderType renderType: FinderQueue.RenderType.values()) {
            builder.then(literal("outlines")
                    .then(literal(renderType.toString()).executes(context -> this.setRenderMode(renderType)))
            );
        }
    }

    private int printRenderMode() {
        sendFeedback("Current render mode is set to [" + FinderQueue.get().renderType + "].", Formatting.AQUA, false);
        return 0;
    }

    private int setRenderMode(FinderQueue.RenderType renderType) {
        FinderQueue.get().renderType = renderType;
        sendFeedback("Set render mode to [" + FinderQueue.get().renderType + "].", Formatting.AQUA, false);
        return 0;
    }

}
