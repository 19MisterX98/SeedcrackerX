package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.util.Log;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.util.Formatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class RenderCommand extends ClientCommand {

    @Override
    public String getName() {
        return "render";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        builder.then(literal("outlines")
                .executes(context -> this.printRenderMode())
        );

        for (Config.RenderType renderType : Config.RenderType.values()) {
            builder.then(literal("outlines")
                    .then(literal(renderType.toString()).executes(context -> this.setRenderMode(renderType)))
            );
        }
    }

    private int printRenderMode() {
        sendFeedback(Log.translate("render.getRenderMode") + " [" + Config.get().render + "].", Formatting.AQUA, false);
        return 0;
    }

    private int setRenderMode(Config.RenderType renderType) {
        Config.get().render = renderType;
        Config.save();
        sendFeedback(Log.translate("render.setRenderMode") + " [" + Config.get().render + "].", Formatting.AQUA, false);
        return 0;
    }

}
