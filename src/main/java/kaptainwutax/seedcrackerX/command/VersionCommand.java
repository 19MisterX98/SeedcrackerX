package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.seedfinding.mccore.version.MCVersion;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.util.Log;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.util.Formatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class VersionCommand extends ClientCommand {

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        for (MCVersion version : MCVersion.values()) {
            if (version.isOlderThan(MCVersion.v1_8)) continue;
            builder.then(literal(version.name).executes(context -> this.setVersion(version)));
        }
    }

    private int setVersion(MCVersion version) {
        Config.get().setVersion(version);
        Config.save();
        ClientCommand.sendFeedback(Log.translate("version.setVersion") + " " + version + ".", Formatting.AQUA, true);
        return 0;
    }

}
