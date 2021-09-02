package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.mcutils.rand.ChunkRand;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.util.Log;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;
import randomreverser.call.java.NextLong;
import randomreverser.device.JavaRandomDevice;
import randomreverser.device.LCGReverserDevice;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CrackerCommand extends ClientCommand {

	@Override
	public String getName() {
		return "cracker";
	}

	@Override
	public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
		builder.then(literal("ON").executes(context -> this.setActive(true)))
				.then(literal("OFF").executes(context -> this.setActive(false)))
			.executes(context -> this.toggleActive());

		builder.then(literal("debug")
					.then(literal("ON").executes(context -> this.setDebug(true)))
					.then(literal("OFF").executes(context -> this.setDebug(false)))
				.executes(context -> this.toggleDebug()));
		builder.then(literal("paper")
				.then(literal("dungeon")
						.then(argument("seed", LongArgumentType.longArg())
								.executes(ctx -> this.paperDungeonStart(LongArgumentType.getLong(ctx, "seed")))
						)
				)

		);
	}

	private int paperDungeonStart(long featureSeed) {
		//wtf did papermc developers smoke to use java random in an attempt to make seed cracking harder
		//Point for them, it was primarily implemented to fix correlation between different features (diamond-clay)
		//and definitely makes world seed cracking impossible with dungeons (when the Random object for world and feature seeds arent created at the same server start)
		//but still...reversing all feature seeds from a single one, really!?
		//I got seed xray working on a server with paper-randomized feature seeds nearly instantly
		Log.debug("this command is useless for most gamers");
		Log.debug("reversing the seed to the initial state of the papermc feature seed randomize random object");
		JavaRandomDevice randomReverser = new JavaRandomDevice();
		randomReverser.addCall(NextLong.withValue(featureSeed));
		randomReverser.streamSeeds(LCGReverserDevice.Process.EVERYTHING).forEach(seed -> {
			ChunkRand rand = new ChunkRand(seed, false);
			rand.advance(-6);
			Log.printSeed("foundStructureSeed", rand.getSeed());
		});
		return 0;
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
