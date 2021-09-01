package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.mcutils.rand.ChunkRand;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.util.Log;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DungeonFeature;
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
	}

	private int paperFeatureStart(long featureSeed) {

		System.out.println("runnin paper command");
		int featureCount = 0;
		for (Identifier identifier : BuiltinRegistries.CONFIGURED_FEATURE.getIds()) {
			ConfiguredFeature feature = BuiltinRegistries.CONFIGURED_FEATURE.get(identifier);
			System.out.println(feature);
			if (feature.getFeature() instanceof DungeonFeature) {
				System.out.println("dungeon feature id is " + featureCount);
				JavaRandomDevice randomReverser = new JavaRandomDevice();
				randomReverser.addCall(NextLong.withValue(featureSeed));
				int finalFeatureCount = featureCount;
				randomReverser.streamSeeds(LCGReverserDevice.Process.EVERYTHING).forEach(seed -> {
					ChunkRand rand = new ChunkRand(seed, false);
					rand.advance(-finalFeatureCount);
					System.out.println(rand.nextLong());
				});
				return 0;
			}
			featureCount++;
		}

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
