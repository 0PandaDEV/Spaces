package net.pandadev.spaces.utils;

import net.pandadev.spaces.Main;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.pandadev.spaces.utils.Configs.chunksConfig;

public class ChunkAPI {

    private static FileConfiguration config = Configs.chunks;

    public static void claimChunk(Player player, Chunk chunk) {
        if (chunksConfig == null) {
            Configs.createChunksConfig();
        }
        String chunkCords = chunk.getX() + ";" + chunk.getZ();

        List chunks = new ArrayList<>();
        if (config.getList(player.getUniqueId() + ".chunks") != null) {
            chunks = config.getList(player.getUniqueId() + ".chunks");
        }

        chunks.add(chunkCords);

        config.set(player.getUniqueId() + ".chunks", chunks);
        Configs.saveChunksConfig();
    }

    public static void unclaimChunk(Player player, Chunk chunk) {
        if (chunksConfig == null) {
            Configs.createChunksConfig();
        }

        String chunkCords = chunk.getX() + ";" + chunk.getZ();

        List chunks = config.getList(player.getUniqueId() + ".chunks");

        chunks.remove(chunkCords);

        config.set(player.getUniqueId() + ".chunks", chunks);
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

        return getAllChunks(chunk.getWorld()).contains(chunk);
    }

    public static boolean isChunkClaimedByPlayer(Player player, Chunk chunk) {
        if (chunksConfig == null) {
            Configs.createChunksConfig();
        }
        String chunkCords = chunk.getX() + ";" + chunk.getZ();
        List<String> playerChunks = config.getStringList(player.getUniqueId() + ".chunks");

        return playerChunks.contains(chunkCords);
    }

    public static List<Chunk> getAllChunks(World world) {
        List<Chunk> chunks = new ArrayList<>();
        for (String key : config.getKeys(false)) {
            List<String> chunkCoordinates = config.getStringList(key + ".chunks");

            for (String coordinate : chunkCoordinates) {
                String[] parts = coordinate.split(";");
                int x = Integer.parseInt(parts[0]);
                int z = Integer.parseInt(parts[1]);
                if (world != null) {
                    Chunk chunk = world.getChunkAt(x, z);
                    chunks.add(chunk);
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
        List<String> chunkCoordinates = config.getStringList(player.getUniqueId() + ".chunks");

        for (String coordinate : chunkCoordinates) {
            String[] parts = coordinate.split(";");
            int x = Integer.parseInt(parts[0]);
            int z = Integer.parseInt(parts[1]);
            Chunk chunk = player.getWorld().getChunkAt(x, z);
            chunks.add(chunk);
        }
        return chunks;
    }

}
