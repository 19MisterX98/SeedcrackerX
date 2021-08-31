package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.mcutils.version.MCVersion;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.util.Log;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public class VersionCommand extends ClientCommand {

	@Override
	public String getName() {
		return "version";
	}

	@Override
	public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
		for(MCVersion version: MCVersion.values()) {
			if(version.isOlderThan(MCVersion.v1_8))continue;
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
