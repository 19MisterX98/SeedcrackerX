package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.seedfinding.mccore.version.MCVersion;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.util.Log;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class VersionCommand extends ClientCommand {

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {
        for (MCVersion version : MCVersion.values()) {
            if (version.isOlderThan(MCVersion.v1_8)) continue;
            builder.then(Commands.literal(version.name).executes(context -> this.setVersion(version)));
        }
    }

    private int setVersion(MCVersion version) {
        Config.get().setVersion(version);
        Config.save();
        ClientCommand.sendFeedback(Log.translate("version.setVersion") + " " + version + ".", ChatFormatting.AQUA, true);
        return 0;
    }
}
