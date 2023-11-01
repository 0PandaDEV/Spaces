package net.pandadev.spaces.utils;

import net.pandadev.spaces.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class Configs {

    public static File chunksConfig;
    public static FileConfiguration chunks;

    public static void createChunksConfig() {
        chunksConfig = new File(Main.getInstance().getDataFolder(), "chunks.yml");
        if (!chunksConfig.exists()) {
            chunksConfig.getParentFile().mkdirs();
            Main.getInstance().saveResource("chunks.yml", false);
        }

        chunks = new YamlConfiguration();
        try {
            chunks.load(chunksConfig);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void saveChunksConfig() {
        if (chunksConfig == null) {
            createChunksConfig();
        }
        try {
            chunks.save(chunksConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
            
}
