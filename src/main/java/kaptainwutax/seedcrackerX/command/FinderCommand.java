package kaptainwutax.seedcrackerX.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kaptainwutax.seedcrackerX.config.Config;
import kaptainwutax.seedcrackerX.finder.Finder;
import kaptainwutax.seedcrackerX.finder.ReloadFinders;
import kaptainwutax.seedcrackerX.util.Log;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.*;

public class FinderCommand extends ClientCommand {
    ReloadFinders reloadFinders = new ReloadFinders();

    @Override
    public String getName() {
        return "finder";
    }
    
    @Override
    public String getUsage() {
        return "/seedcracker finder <type|category|reload>";
    }
 
    public String getUsageType() {
        return "/seedcracker finder type <finder_type> [ON|OFF]";
    }
 
    public String getUsageCategory() {
        return "/seedcracker finder category <category> [ON|OFF]";
    }

    @Override
    public void build(LiteralArgumentBuilder<FabricClientCommandSource> builder) {
        LiteralArgumentBuilder<FabricClientCommandSource> typeNode = literal("type")
                .executes(context -> {
                    sendFeedback("Usage: " + this.getUsageType(), ChatFormatting.RED);
                    return 1;
                });
        for (Finder.Type finderType : Finder.Type.values()) {
            typeNode.then(literal(finderType.toString())
                    .then(literal("ON").executes(context -> this.setFinderType(finderType, true, true)))
                    .then(literal("OFF").executes(context -> this.setFinderType(finderType, false, true)))
                    .executes(context -> this.printFinderType(finderType)));
        }
        builder.then(typeNode);
 
        LiteralArgumentBuilder<FabricClientCommandSource> categoryNode = literal("category")
                .executes(context -> {
                    sendFeedback("Usage: " + this.getUsageCategory(), ChatFormatting.RED);
                    return 1;
                });
        for (Finder.Category finderCategory : Finder.Category.values()) {
            categoryNode.then(literal(finderCategory.toString())
                    .then(literal("ON").executes(context -> this.setFinderCategory(finderCategory, true)))
                    .then(literal("OFF").executes(context -> this.setFinderCategory(finderCategory, false)))
                    .executes(context -> this.printFinderCategory(finderCategory)));
        }
        builder.then(categoryNode);
 
        builder.then(literal("reload").executes(context -> this.reload()));
    }

    private int printFinderCategory(Finder.Category finderCategory) {
        Finder.Type.getForCategory(finderCategory).forEach(this::printFinderType);
        return 0;
    }

    private int printFinderType(Finder.Type finderType) {
        sendFeedback(Log.translate("finder.isFinder").formatted(Log.translate(finderType.nameKey)) + " [" + String.valueOf(finderType.enabled.get()).toUpperCase() + "].", ChatFormatting.AQUA);
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
        sendFeedback(Log.translate("finder.setFinder").formatted(Log.translate(finderType.nameKey)) + " [" + String.valueOf(flag).toUpperCase() + "].", ChatFormatting.AQUA);
        return 0;
    }

    private int reload() {
        reloadFinders.reload();
        return 0;
    }

}
