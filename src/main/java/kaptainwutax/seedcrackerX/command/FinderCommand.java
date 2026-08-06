package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.finder.ReloadFinders;
import kaptainwutax.seedcrackerX.util.Log;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class FinderCommand extends ClientCommand {
    ReloadFinders reloadFinders = new ReloadFinders();

    @Override
    public String getName() {
        return "finder";
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {
        for (Finder.Type finderType : Finder.Type.values()) {
            builder.then(Commands.literal("type")
                    .then(Commands.literal(finderType.toString())
                            .then(Commands.literal("ON").executes(context -> this.setFinderType(finderType, true, true)))
                            .then(Commands.literal("OFF").executes(context -> this.setFinderType(finderType, false, true)))
                            .executes(context -> this.printFinderType(finderType)))
            );
        }

        for (Finder.Category finderCategory : Finder.Category.values()) {
            builder.then(Commands.literal("category")
                    .then(Commands.literal(finderCategory.toString())
                            .then(Commands.literal("ON").executes(context -> this.setFinderCategory(finderCategory, true)))
                            .then(Commands.literal("OFF").executes(context -> this.setFinderCategory(finderCategory, false)))
                            .executes(context -> this.printFinderCategory(finderCategory)))
            );
        }
        builder.then(Commands.literal("reload").executes(context -> this.reload()));
    }

    private int printFinderCategory(Finder.Category finderCategory) {
        Finder.Type.getForCategory(finderCategory).forEach(this::printFinderType);
        return 0;
    }

    private int printFinderType(Finder.Type finderType) {
        sendFeedback(Log.translate("finder.isFinder").formatted(Log.translate(finderType.nameKey)) + " [" + String.valueOf(finderType.enabled.get()).toUpperCase() + "].", ChatFormatting.AQUA, false);
        return 0;
    }

    private int setFinderCategory(Finder.Category finderCategory, boolean flag) {
        Finder.Type.getForCategory(finderCategory).forEach(finderType -> this.setFinderType(finderType, flag, false));
        Config.save();
        return 0;
    }

    private int setFinderType(Finder.Type finderType, boolean flag, boolean save) {
        finderType.enabled.set(flag);
        if (save) Config.save();
        sendFeedback(Log.translate("finder.setFinder").formatted(Log.translate(finderType.nameKey)) + " [" + String.valueOf(flag).toUpperCase() + "].", ChatFormatting.AQUA, false);
        return 0;
    }

    private int reload() {
        reloadFinders.reload();
        return 0;
    }
}
