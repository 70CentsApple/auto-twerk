package net.apple70cents.auto_twerk.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.apple70cents.auto_twerk.AutoTwerk;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author 70CentsApple
 */
public class ModConfig {
    private static final File file = new File(net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().toFile(), "auto_twerk.json");

    public static File getFile() {
        return file;
    }

    private static ModConfig INSTANCE = new ModConfig();

    /**
     * 保存配置
     */

    public static void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        AutoTwerk.LOGGER.info("[AutoTwerk] Saving configs.");
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(INSTANCE, writer);
        } catch (Exception e) {
            AutoTwerk.LOGGER.error("[AutoTwerk] Couldn't save config.");
            e.printStackTrace();
        }
    }

    /**
     * 尝试读取配置
     */
    public static void load() {
        AutoTwerk.LOGGER.info("[AutoTwerk] Loading configs...");
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            INSTANCE = gson.fromJson(reader, ModConfig.class);
            if (INSTANCE == null) {
                INSTANCE = new ModConfig();
            }
        } catch (Exception e) {
            if (file.exists()) {
                AutoTwerk.LOGGER.warn("[AutoTwerk] Couldn't understand the config.");
                e.printStackTrace();
            } else {
                AutoTwerk.LOGGER.warn("[AutoTwerk] Couldn't find the config.");
            }
        }
    }

    /**
     * 获取配置实例
     *
     * @return 配置实例
     */
    public static ModConfig get() {
        return INSTANCE;
    }

    public boolean autoTwerkEnabled = false;
    public int interval = 10;

}
