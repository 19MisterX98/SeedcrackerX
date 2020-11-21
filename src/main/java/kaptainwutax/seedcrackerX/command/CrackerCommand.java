package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.profile.config.ConfigScreen;
import net.minecraft.server.command.ServerCommandSource;


import static net.minecraft.server.command.CommandManager.literal;

public class CrackerCommand extends ClientCommand {

	@Override
	public String getName() {
		return "cracker";
	}

	@Override
	public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
		builder.then(literal("ON").executes(context -> this.setActive(true)))
				.then(literal("OFF").executes(context -> this.setActive(false)));

		builder.then(literal("debug")
					.then(literal("ON").executes(context -> this.setDebug(true)))
					.then(literal("OFF").executes(context -> this.setDebug(false)))
				.executes(context -> this.toggleDebug()));
	}


	private int setActive(boolean flag) {
		SeedCracker.get().setActive(flag);
		return 0;
	}

	private int toggleDebug() {
		ConfigScreen.getConfig().setDEBUG(!ConfigScreen.getConfig().isDEBUG());
		return 0;
	}

	private int setDebug(boolean debug) {
		ConfigScreen.getConfig().setDEBUG(debug);
		return 0;
	}
}
