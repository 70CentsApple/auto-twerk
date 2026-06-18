package net.apple70cents.auto_twerk.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class MessageUtils {
    private MessageUtils() {}

    public static void sendToActionbar(Component text) {
        if (Minecraft.getInstance().player == null) {
            return;
        }
//? if >=26.1 {
        Minecraft.getInstance().player.sendOverlayMessage(text);
//?} else {
        /*Minecraft.getInstance().player.displayClientMessage(text, true);
*///?}
    }
}
