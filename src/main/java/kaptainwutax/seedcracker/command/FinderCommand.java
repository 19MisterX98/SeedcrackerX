package kaptainwutax.seedcracker.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcracker.finder.Finder;
import kaptainwutax.seedcracker.finder.FinderQueue;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public class FinderCommand extends ClientCommand {

    @Override
    public String getName() {
        return "finder";
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        for(Finder.Type finderType: Finder.Type.values()) {
            builder.then(literal("type")
                    .then(literal(finderType.toString())
                            .then(literal("ON").executes(context -> this.setFinderType(finderType, true)))
                            .then(literal("OFF").executes(context -> this.setFinderType(finderType, false))))
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
    }

    private int printFinderCategory(Finder.Category finderCategory) {
        Finder.Type.getForCategory(finderCategory).forEach(this::printFinderType);
        return 0;
    }

    private int printFinderType(Finder.Type finderType) {
        sendFeedback("Finder " + finderType + " is set to [" + String.valueOf(FinderQueue.get().finderProfile.getActive(finderType)).toUpperCase() + "].", Formatting.AQUA,false);
        return 0;
    }

    private int setFinderCategory(Finder.Category finderCategory, boolean flag) {
        Finder.Type.getForCategory(finderCategory).forEach(finderType -> this.setFinderType(finderType, flag));
        return 0;
    }

    private int setFinderType(Finder.Type finderType, boolean flag) {
        if(FinderQueue.get().finderProfile.setActive(finderType, flag)) {
            sendFeedback("Finder " + finderType + " has been set to [" + String.valueOf(flag).toUpperCase() + "].", Formatting.AQUA, false);
        } else {
            sendFeedback("Your current finder profile is locked and cannot be modified. Please make a copy first.", Formatting.RED, false);
        }

        return 0;
    }

}
