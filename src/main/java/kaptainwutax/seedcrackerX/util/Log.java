package kaptainwutax.seedcrackerX.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;

import java.util.regex.Pattern;

public class Log {

    public static void debug(String message) {
        PlayerEntity player = getPlayer();

        if (player != null) {
            schedule(() -> player.sendMessage(Text.literal(message), false));
        }
    }

    public static void warn(String translateKey, Object... args) {
        String message = translate(translateKey).formatted(args);
        PlayerEntity player = getPlayer();

        if (player != null) {
            schedule(() -> player.sendMessage(Text.literal(message).formatted(Formatting.GREEN), false));
        }
    }

    public static void warn(String translateKey) {
        warn(translateKey, new Object[]{});
    }

    public static void error(String translateKey) {
        String message = translate(translateKey);
        PlayerEntity player = getPlayer();

        if (player != null) {
            schedule(() -> player.sendMessage(Text.literal(message).formatted(Formatting.RED), false));
        }
    }

    public static void printSeed(String translateKey, long seedValue) {
        String message = translate(translateKey);
        String[] data = message.split(Pattern.quote("${SEED}"));
        String seed = String.valueOf(seedValue);
        Text text = Texts.bracketed((Text.literal(seed)).styled(style -> style.withColor(Formatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, seed)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("chat.copy.click"))).withInsertion(seed)));

        PlayerEntity player = getPlayer();

        if (player != null) {
            MutableText text1 = Text.literal(data[0]).append(text);
            if (data.length > 1) {
                text1.append(Text.literal(data[1]));
            }
            schedule(() -> player.sendMessage(text1, false));
        }
    }

    public static void printDungeonInfo(String message) {
        Text text = Texts.bracketed((Text.literal(message)).styled(style -> style.withColor(Formatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, message)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("chat.copy.click"))).withInsertion(message)));

        PlayerEntity player = getPlayer();

        if (player != null) {
            schedule(() -> player.sendMessage(text, false));
        }
    }

    public static String translate(String translateKey) {
        return Language.getInstance().get(translateKey);
    }

    private static void schedule(Runnable runnable) {
        MinecraftClient.getInstance().execute(runnable);
    }

    private static PlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }

}
