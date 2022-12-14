package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.util.Log;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.util.Formatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CrackerCommand extends ClientCommand {

    @Override
    public String getName() {
        return "cracker";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        builder.then(literal("ON").executes(context -> this.setActive(true)))
                .then(literal("OFF").executes(context -> this.setActive(false)))
                .executes(context -> this.toggleActive());

        builder.then(literal("debug")
                .then(literal("ON").executes(context -> this.setDebug(true)))
                .then(literal("OFF").executes(context -> this.setDebug(false)))
                .executes(context -> this.toggleDebug()));
    }

    private void feedback(boolean success, boolean flag) {
        String action = Log.translate(flag ? "cracker.enabled" : "cracker.disabled");
        if (success) {
            sendFeedback(Log.translate("cracker.successfully") + action, Formatting.GREEN, false);
        } else {
            sendFeedback(Log.translate("cracker.already") + action, Formatting.RED, false);
        }
        Config.save();
    }

    private int setActive(boolean flag) {
        feedback(Config.get().active != flag, flag);
        Config.get().active = flag;
        return 0;
    }

    private int toggleActive() {
        Config.get().active = !Config.get().active;
        feedback(true, Config.get().active);
        return 0;
    }

    private int setDebug(boolean flag) {
        feedback(Config.get().debug != flag, flag);
        Config.get().debug = flag;
        return 0;
    }

    private int toggleDebug() {
        Config.get().debug = !Config.get().debug;
        feedback(true, Config.get().debug);
        return 0;
    }
}
