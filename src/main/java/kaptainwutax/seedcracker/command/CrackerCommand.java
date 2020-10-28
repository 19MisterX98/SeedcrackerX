package kaptainwutax.seedcracker.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcracker.SeedCracker;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.finder.FinderQueue;
import kaptainwutax.seedcracker.util.Log;
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
		builder.then(literal("debug").executes(context -> this.debug()));
	}


	private int setActive(boolean flag) {
		SeedCracker.get().setActive(flag);
		return 0;
	}

	private int debug() {
		Log.warn(String.valueOf(FinderQueue.get().finderProfile.getActive(Finder.Type.DUNGEON)).toUpperCase());

		return 0;
	}

}
