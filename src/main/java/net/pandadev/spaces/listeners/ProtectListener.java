package net.pandadev.spaces.listeners;

import net.pandadev.spaces.Main;
import net.pandadev.spaces.utils.ChunkAPI;
import net.pandadev.spaces.utils.Configs;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ProtectListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!ChunkAPI.isChunkClaimed(event.getPlayer().getLocation().getChunk())) return;
        if (ChunkAPI.isChunkClaimedByPlayer(event.getPlayer(), event.getPlayer().getLocation().getChunk())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Main.getPrefix() + "§cYou can't break blocks here");
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!ChunkAPI.isChunkClaimed(event.getPlayer().getLocation().getChunk())) return;
        if (ChunkAPI.isChunkClaimedByPlayer(event.getPlayer(), event.getPlayer().getLocation().getChunk())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(Main.getPrefix() + "§cYou can't place blocks here");

    }

    @EventHandler
    public void onPlayerCropTrample(PlayerInteractEvent event) {
        if (!ChunkAPI.isChunkClaimed(event.getPlayer().getLocation().getChunk())) return;
        if (ChunkAPI.isChunkClaimedByPlayer(event.getPlayer(), event.getPlayer().getLocation().getChunk())) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Material material = event.getClickedBlock().getType();
            if (material == Material.ACACIA_DOOR || material == Material.BIRCH_DOOR || material == Material.DARK_OAK_DOOR
                    || material == Material.JUNGLE_DOOR || material == Material.OAK_DOOR || material == Material.SPRUCE_DOOR
                    || material == Material.IRON_DOOR || material == Material.ACACIA_TRAPDOOR || material == Material.BIRCH_TRAPDOOR
                    || material == Material.DARK_OAK_TRAPDOOR || material == Material.JUNGLE_TRAPDOOR || material == Material.OAK_TRAPDOOR
                    || material == Material.SPRUCE_TRAPDOOR || material == Material.IRON_TRAPDOOR || material == Material.LEVER
                    || material == Material.STONE_BUTTON || material == Material.OAK_BUTTON || material == Material.SPRUCE_BUTTON
                    || material == Material.BIRCH_BUTTON || material == Material.JUNGLE_BUTTON || material == Material.ACACIA_BUTTON
                    || material == Material.DARK_OAK_BUTTON || material == Material.POLISHED_BLACKSTONE_BUTTON) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Main.getPrefix() + "§cYou can't interact with this block");
            }
        }

        if (event.getAction() != Action.PHYSICAL) return;
        if (event.getClickedBlock() != null && event.getClickedBlock().getType().toString().matches("SOIL|FARMLAND"))
            event.setCancelled(true);
    }
}
