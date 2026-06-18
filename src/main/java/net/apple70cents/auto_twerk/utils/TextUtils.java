package net.apple70cents.auto_twerk.utils;

import net.minecraft.network.chat.Component;
//? if <1.19 {
/*import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
*///?}

/**
 * Version-agnostic text helpers, modeled after ChatTools' TextUtils.
 * <p>
 * Hides the {@code Component.literal} / {@code new TextComponent(...)} and
 * {@code Component.translatable} / {@code new TranslatableComponent(...)}
 * split so callers don't have to litter stonecutter conditionals everywhere.
 *
 * @author 70CentsApple
 */
public class TextUtils {
    /** Shared translation-key prefix for this mod. */
    public static final String PREFIX = "autotwerk.";

    public static Component literal(String str) {
//? if >=1.19 {
        return Component.literal(str);
//?} else {
        /*return new TextComponent(str);
*///?}
    }

    public static Component transWithPrefix(String str, String prefix, Object... args) {
        String key = prefix + str;
//? if >=1.19 {
        return Component.translatable(key, args);
//?} else {
        /*return new TranslatableComponent(key, args);
*///?}
    }

    public static Component transWithPrefix(String str, String prefix) {
        return transWithPrefix(str, prefix, new Object[0]);
    }

    public static Component trans(String str, Object... args) {
        return transWithPrefix(str, PREFIX, args);
    }

    public static Component trans(String str) {
        return transWithPrefix(str, PREFIX);
    }
}
