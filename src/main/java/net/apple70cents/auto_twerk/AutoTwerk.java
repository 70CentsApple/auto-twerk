package net.apple70cents.auto_twerk;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.apple70cents.auto_twerk.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
//#if MC>=11900
import net.minecraft.text.Text;
//#else
//$$import net.minecraft.text.TranslatableText;
//#endif
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

//#if MC>=11900
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
//#else
//$$ import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
//$$ import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;
//$$ import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
//#endif

/**
 * @author 70CentsApple
 */
public class AutoTwerk implements ClientModInitializer {

    public static final String MOD_ID = "auto_twerk";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    private static int timer = -1;

    @Override
    public void onInitializeClient() {
        ModConfig.load();
        KeyBinding keybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("autotwerk.text.toggle", GLFW.GLFW_KEY_UNKNOWN, "autotwerk.text.category_title"));

        ClientTickEvents.START_WORLD_TICK.register(c -> onEachTick());
        ClientTickEvents.START_WORLD_TICK.register(c -> {
            while (keybinding.wasPressed()) {
                ModConfig.get().autoTwerkEnabled = !ModConfig.get().autoTwerkEnabled;
                if (MinecraftClient.getInstance().player == null) {
                    return;
                }
                // unsneak
                KeyBinding key = MinecraftClient.getInstance().options.sneakKey;
                key.setPressed(false);
                MinecraftClient.getInstance().player.sendMessage(ModConfig.get().autoTwerkEnabled ?
                                //#if MC>=11900
                                Text.translatable("autotwerk.feedback.on") : Text.translatable("autotwerk.feedback.off")
                        //#else
                        //$$new TranslatableText("autotwerk.feedback.on") : new TranslatableText("autotwerk.feedback.off")
                        //#endif

                        , true);
            }
        });

        //#if MC>=11900
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(literal("autotwerk")
                    //#else
                    //$$ClientCommandManager.DISPATCHER.register(literal("autotwerk")
                    //#endif

                    // on
                    .then(literal("on").executes(t -> {
                        ModConfig.get().autoTwerkEnabled = true;
                        t.getSource().sendFeedback(
                                //#if MC>=11900
                                Text.translatable("autotwerk.feedback.on")
                        //#else
                        //$$new TranslatableText("autotwerk.feedback.on")
                        //#endif
                        );
                        ModConfig.save();
                        return 1;
                    }))
                    // off
                    .then(literal("off").executes(t -> {
                        ModConfig.get().autoTwerkEnabled = false;
                        t.getSource().sendFeedback(
                                //#if MC>=11900
                                 Text.translatable("autotwerk.feedback.off")
                        //#else
                        //$$new TranslatableText("autotwerk.feedback.off")
                        //#endif
                        );
                        ModConfig.save();
                        return 1;
                    }))
                    // setinterval
                    .then(literal("setinterval").then(argument("interval", IntegerArgumentType.integer(0, 9999)).executes(t -> {
                        ModConfig.get().interval = IntegerArgumentType.getInteger(t, "interval");
                        t.getSource().sendFeedback(
                                //#if MC>=11900
                                Text.translatable("autotwerk.feedback.interval"
                                        //#else
                                        //$$new TranslatableText("autotwerk.feedback.interval"
                                        //#endif

                                        , ModConfig.get().interval));
                        ModConfig.save();
                        return 1;
                    }))));
            //#if MC>=11900
        });
        //#endif


    }

    private void onEachTick() {
        if (!ModConfig.get().autoTwerkEnabled || timer++ < ModConfig.get().interval) {
            return;
        }
        KeyBinding key = MinecraftClient.getInstance().options.sneakKey;
        key.setPressed(!key.isPressed());
        timer = -1;
    }

}
