package net.pandadev.spaces.utils;

import net.pandadev.spaces.Main;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.util.*;

import static net.pandadev.spaces.utils.Configs.chunksConfig;

public class ChunkAPI {

    private static FileConfiguration config = Configs.chunks;

    public static void claimChunk(Player player, Chunk chunk) {
        if (chunksConfig == null) {
            Configs.createChunksConfig();
        }

        String chunkCords = chunk.getX() + ";" + chunk.getZ();
        String uniqueKey = chunk.getWorld().getName() + "," + chunkCords;

        config.set(player.getUniqueId() + ".chunks." + uniqueKey, chunkCords);
        Configs.saveChunksConfig();
    }

    public static void unclaimChunk(Player player, Chunk chunk) {
        if (chunksConfig == null) {
            Configs.createChunksConfig();
        }

        String chunkCords = chunk.getX() + ";" + chunk.getZ();
        String uniqueKey = chunk.getWorld().getName() + "," + chunkCords;

        config.set(player.getUniqueId() + ".chunks." + uniqueKey, null);
        Configs.saveChunksConfig();
    }

    public static void unclaimAll(Player player) {
        if (chunksConfig == null) {
            Configs.createChunksConfig();
        }

        config.set(player.getUniqueId() + ".chunks", null);
        Configs.saveChunksConfig();
    }

    public static boolean isChunkClaimed(Chunk chunk) {
        if (chunksConfig == null) {
            Configs.createChunksConfig();
        }

        String chunkCords = chunk.getX() + ";" + chunk.getZ();
        String uniqueKey = chunk.getWorld().getName() + "," + chunkCords;

        for (String key : config.getKeys(false)) {
            if (config.contains(key + ".chunks." + uniqueKey)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isChunkClaimedByPlayer(Player player, Chunk chunk) {
        if (chunksConfig == null) {
            Configs.createChunksConfig();
        }

        String chunkCords = chunk.getX() + ";" + chunk.getZ();
        String uniqueKey = chunk.getWorld().getName() + "," + chunkCords;

        return config.contains(player.getUniqueId() + ".chunks." + uniqueKey);
    }

    public static List<String> getAllChunks(World world) {
        List<String> chunks = new ArrayList<>();
        for (String key : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(key + ".chunks");
            if (section != null) {
                for (String chunkKey : section.getKeys(false)) {
                    String worldName = chunkKey.split(",")[0];
                    if (worldName.equals(world.getName())) {
                        chunks.add(section.getString(chunkKey));
                    }
                }
            }
        }
        return chunks;
    }

    public static List<Chunk> getAllPlayerChunks(Player player) {
        List<Chunk> chunks = new ArrayList<>();
        if (chunksConfig == null) {
            Configs.createChunksConfig();
        }

        ConfigurationSection section = config.getConfigurationSection(player.getUniqueId() + ".chunks");

        if (section != null) {
            for (String chunkKey : section.getKeys(false)) {
                String chunkCords = section.getString(chunkKey);
                String[] split = chunkCords.split(";");
                int x = Integer.parseInt(split[0]);
                int z = Integer.parseInt(split[1]);

                String worldName = chunkKey.split(",")[0];
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    Chunk chunk = world.getChunkAt(x, z);
                    chunks.add(chunk);
                }
            }
        }

        return chunks;
    }
}