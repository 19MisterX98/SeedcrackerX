package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.finder.ReloadFinders;
import kaptainwutax.seedcrackerX.config.Config;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public class FinderCommand extends ClientCommand {
    ReloadFinders reloadFinders = new ReloadFinders();

    @Override
    public String getName() {
        return "finder";
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        for(Finder.Type finderType: Finder.Type.values()) {
            builder.then(literal("type")
                    .then(literal(finderType.toString())
                            .then(literal("ON").executes(context -> this.setFinderType(finderType, true, true)))
                            .then(literal("OFF").executes(context -> this.setFinderType(finderType, false, true))))
                    .executes(context -> this.printFinderType(finderType))
            );
        }

        for(Finder.Category finderCategory: Finder.Category.values()) {
            builder.then(literal("category")
                    .then(literal(finderCategory.toString())
                            .then(literal("ON").executes(context -> this.setFinderCategory(finderCategory, true)))
                            .then(literal("OFF").executes(context -> this.setFinderCategory(finderCategory, false))))
                    .executes(context -> this.printFinderCategory(finderCategory))
            );
        }
        builder.then(literal("reload").executes(context -> this.reload()));
    }

    private int printFinderCategory(Finder.Category finderCategory) {
        Finder.Type.getForCategory(finderCategory).forEach(this::printFinderType);
        return 0;
    }

    private int printFinderType(Finder.Type finderType) {
        sendFeedback("Finder " + finderType + " is set to [" + String.valueOf(finderType.enabled).toUpperCase() + "].", Formatting.AQUA,false);
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
        sendFeedback("Finder " + finderType + " has been set to [" + String.valueOf(flag).toUpperCase() + "].", Formatting.AQUA, false);
        return 0;
    }

    private int reload() {
        reloadFinders.reload();
        return 0;
    }

}
