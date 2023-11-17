package net.pandadev.spaces.utils;

import net.pandadev.spaces.Main;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ParticleManager implements Listener {
    private static HashMap<UUID, BukkitTask> tasks = new HashMap<>();
    private static HashMap<UUID, Chunk> chunks = new HashMap<>();
    public static void claim(Player player) {
        stop(player);

        Location location = player.getLocation();
        Chunk chunk = location.getChunk();

        chunks.put(player.getUniqueId(), chunk);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                List<Chunk> claimedChunks = ChunkAPI.getAllPlayerChunks(player).stream()
                        .filter(c -> c.getWorld().equals(player.getWorld()))
                        .collect(Collectors.toList());

                for (Chunk claimedChunk : claimedChunks) {
                    if (!claimedChunk.equals(chunks.get(player.getUniqueId()))) {
                        displayParticleBorder(player, claimedChunk, Color.WHITE);
                    }
                }
                displayParticleBorder(player, chunks.get(player.getUniqueId()), Color.GREEN);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);

        tasks.put(player.getUniqueId(), task);
    }

    public static void unclaim(Player player) {
        stop(player);

        Location location = player.getLocation();
        Chunk chunk = location.getChunk();

        chunks.put(player.getUniqueId(), chunk);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                List<Chunk> claimedChunks = ChunkAPI.getAllPlayerChunks(player).stream()
                        .filter(c -> c.getWorld().equals(player.getWorld()))
                        .collect(Collectors.toList());

                for (Chunk claimedChunk : claimedChunks) {
                    if (!claimedChunk.equals(chunks.get(player.getUniqueId()))) {
                        displayParticleBorder(player, claimedChunk, Color.WHITE);
                    }
                }
                displayParticleBorder(player, chunks.get(player.getUniqueId()), Color.RED);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);

        tasks.put(player.getUniqueId(), task);
    }

    static boolean unclaimall = false;

    public static void unclaimAll(Player player) {
        stop(player);

        Location location = player.getLocation();
        Chunk chunk = location.getChunk();

        chunks.put(player.getUniqueId(), chunk);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                List<Chunk> claimedChunks = ChunkAPI.getAllPlayerChunks(player).stream()
                        .filter(c -> c.getWorld().equals(player.getWorld()))
                        .collect(Collectors.toList());

                for (Chunk claimedChunk : claimedChunks) {
                    if (!claimedChunk.equals(chunks.get(player.getUniqueId()))) {
                        displayParticleBorder(player, claimedChunk, Color.RED);
                    }
                }
                displayParticleBorder(player, chunks.get(player.getUniqueId()), Color.RED);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);

        tasks.put(player.getUniqueId(), task);

        unclaimall = true;
    }

    public static void displayAllClaimedChunks(Player player) {
        stop(player);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                List<Chunk> claimedChunks = ChunkAPI.getAllPlayerChunks(player).stream()
                        .filter(c -> c.getWorld().equals(player.getWorld()))
                        .collect(Collectors.toList());

                for (Chunk claimedChunk : claimedChunks) {
                    displayParticleBorder(player, claimedChunk, java.awt.Color.WHITE);
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);

        tasks.put(player.getUniqueId(), task);
    }

    private static void displayParticleBorder(Player player, Chunk chunk, java.awt.Color color) {
        World world = player.getWorld();
        if (chunk.getWorld().equals(world)) {
            int chunkX = chunk.getX() * 16;
            int chunkZ = chunk.getZ() * 16;
            double playerY = player.getEyeLocation().getY() - 1;

            for (int i = chunkX; i < chunkX + 16; i++) {
                player.spawnParticle(Particle.REDSTONE, i, playerY, chunkZ, 0, 0, 0, 0, new Particle.DustOptions(org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()), 1));
                player.spawnParticle(Particle.REDSTONE, i, playerY, chunkZ + 16, 0, 0, 0, 0, new Particle.DustOptions(org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()), 1));
            }
            for (int i = chunkZ; i < chunkZ + 16; i++) {
                player.spawnParticle(Particle.REDSTONE, chunkX, playerY, i, 0, 0, 0, 0, new Particle.DustOptions(org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()), 1));
                player.spawnParticle(Particle.REDSTONE, chunkX + 16, playerY, i, 0, 0, 0, 0, new Particle.DustOptions(org.bukkit.Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()), 1));
            }
        }
    }

    public static void stop(Player player) {
        BukkitTask task = tasks.get(player.getUniqueId());
        if (task != null) {
            task.cancel();
            tasks.remove(player.getUniqueId());
        }
    }

    boolean inChunk = false;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Chunk fromChunk = event.getFrom().getChunk();
        Chunk toChunk = event.getTo().getChunk();

        if (chunks.containsKey(player.getUniqueId())) {
            if (!fromChunk.equals(chunks.get(player.getUniqueId()))) {
                stop(player);
            }
        }

        if (unclaimall) {
            if (ChunkAPI.getAllPlayerChunks(player).contains(toChunk)) {
                if (inChunk) {
                    unclaimall = false;
                    inChunk = false;
                    stop(player);
                }
                inChunk = true;
            }
        }


    }
}
