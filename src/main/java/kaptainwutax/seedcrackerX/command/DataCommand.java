package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kaptainwutax.seedcrackerX.SeedCracker;
import kaptainwutax.seedcrackerX.cracker.storage.DataStorage;
import kaptainwutax.seedcrackerX.finder.FinderQueue;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public class DataCommand extends ClientCommand {

	@Override
	public String getName() {
		return "data";
	}

	@Override
	public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
		builder.then(literal("clear")
				.executes(this::clear)
		);

		builder.then(literal("bits")
				.executes(this::printBits)
		);
	}

	public int clear(CommandContext<ServerCommandSource> context) {
		SeedCracker.get().getDataStorage().clear();
		FinderQueue.get().finderProfile.deleteFinders();
		sendFeedback("Cleared data storage", Formatting.GREEN, false);
		return 0;
	}

	private int printBits(CommandContext<ServerCommandSource> context) {
		DataStorage s = SeedCracker.get().getDataStorage();
		String message = "You currently have collected " + (int)s.getBaseBits() + " bits out of " + (int)s.getWantedBits() + ".";
		sendFeedback(message, Formatting.GREEN, false);
		return 0;
	}

}

