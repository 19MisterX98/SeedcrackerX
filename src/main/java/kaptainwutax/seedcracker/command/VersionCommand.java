package kaptainwutax.seedcracker.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcracker.Features;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedutils.mc.MCVersion;
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
			if(version.isOlderThan(MCVersion.v1_13))continue;
			builder.then(literal(version.name).executes(context -> this.setVersion(version)));
		}
	}

	private int setVersion(MCVersion version) {
		SeedCracker.MC_VERSION = version;
		Features.init(SeedCracker.MC_VERSION);
		ClientCommand.sendFeedback("Changed version to " + version + ".", Formatting.AQUA, true);
		return 0;
	}

}
