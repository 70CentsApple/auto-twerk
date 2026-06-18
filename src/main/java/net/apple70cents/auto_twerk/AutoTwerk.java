package net.apple70cents.auto_twerk;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.apple70cents.auto_twerk.config.ModConfig;
import net.apple70cents.auto_twerk.utils.MessageUtils;
import net.apple70cents.auto_twerk.utils.TextUtils;

//? if FABRIC {
/*import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//? if >=26.1 {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;
//?} elif >=1.19 {
/^import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
^///?} else {
/^import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
^///?}
*///?} elif NEOFORGE {
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
//?}

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
//? if >=1.19 {
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
//?}
//? if >=1.21.9 {
import net.minecraft.resources.Identifier;
//?}

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

/**
 * @author 70CentsApple
 */
//? if NEOFORGE {
@Mod("auto_twerk")
//?}
public class AutoTwerk
//? if FABRIC {
/*implements ClientModInitializer
*///?}
{

    public static final String MOD_ID = "auto_twerk";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    private static int timer = -1;

    public static final KeyMapping TOGGLE_KEY = new KeyMapping(
            "autotwerk.text.toggle",
            GLFW.GLFW_KEY_UNKNOWN,
//? if >=1.21.9 {
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "controls"))
//?} else {
            /*"autotwerk.text.category_title"
*///?}
    );

//? if FABRIC {
    /*@Override
    public void onInitializeClient() {
        ModConfig.load();
    //? if >=26.1 {
        KeyMappingHelper.registerKeyMapping(TOGGLE_KEY);
    //?} else {
        /^KeyBindingHelper.registerKeyBinding(TOGGLE_KEY);
        ^///?}

        ClientTickEvents.START_CLIENT_TICK.register(c -> onEachTick());
        ClientTickEvents.START_CLIENT_TICK.register(c -> handleToggleKey());

    //? if >=1.19 {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(getBuilder(registryAccess)));
    //?} else {
        /^ClientCommandManager.DISPATCHER.register(getBuilder());
        ^///?}
    }
*///?} elif NEOFORGE {
    public AutoTwerk(IEventBus modBus) {
        ModConfig.load();

        modBus.addListener((RegisterKeyMappingsEvent event) -> event.register(TOGGLE_KEY));

        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Pre event) -> onEachTick());
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Pre event) -> handleToggleKey());
        NeoForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) ->
                event.getDispatcher().register(getBuilder(event.getBuildContext())));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
//?}

    private static void handleToggleKey() {
        while (TOGGLE_KEY.consumeClick()) {
            toggleEnabled();
        }
    }

    private static void toggleEnabled() {
        ModConfig.get().autoTwerkEnabled = !ModConfig.get().autoTwerkEnabled;
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().options.keyShift.setDown(false);
        }
        MessageUtils.sendToActionbar(
                ModConfig.get().autoTwerkEnabled
                        ? TextUtils.trans("feedback.on")
                        : TextUtils.trans("feedback.off"));
        ModConfig.save();
    }

    private static LiteralArgumentBuilder<
//? if FABRIC {
            /*FabricClientCommandSource
*///?} elif NEOFORGE {
            CommandSourceStack
//?}
            > getBuilder(
//? if >=1.19 {
            CommandBuildContext buildContext
//?}
    ) {
        return literal("autotwerk")
                .then(literal("on").executes(t -> {
                    ModConfig.get().autoTwerkEnabled = true;
                    MessageUtils.sendToActionbar(TextUtils.trans("feedback.on"));
                    ModConfig.save();
                    return 1;
                }))
                .then(literal("off").executes(t -> {
                    ModConfig.get().autoTwerkEnabled = false;
                    MessageUtils.sendToActionbar(TextUtils.trans("feedback.off"));
                    ModConfig.save();
                    return 1;
                }))
                .then(literal("toggle").executes(t -> {
                    toggleEnabled();
                    return 1;
                }))
                .then(literal("setinterval").then(
                        argument("interval", IntegerArgumentType.integer(0, 9999)).executes(t -> {
                            ModConfig.get().interval = IntegerArgumentType.getInteger(t, "interval");
                            MessageUtils.sendToActionbar(
                                    TextUtils.trans("feedback.interval", ModConfig.get().interval));
                            ModConfig.save();
                            return 1;
                        })));
    }

    private static void onEachTick() {
        if (!ModConfig.get().autoTwerkEnabled || timer++ < ModConfig.get().interval) {
            return;
        }
        KeyMapping key = Minecraft.getInstance().options.keyShift;
        key.setDown(!key.isDown());
        timer = -1;
    }

}
