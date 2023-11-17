package net.pandadev.spaces.commands;

import net.pandadev.spaces.Main;
import net.pandadev.spaces.utils.ChunkAPI;
import net.pandadev.spaces.utils.ParticleManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClaimCommand implements CommandExecutor {

    private List<Player> confirming = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getPrefix() + "§6This command can only be run by a player!");
            return false;
        }

        Player player = (Player) (sender);

        if (args.length == 0 && label.equalsIgnoreCase("claim")){
            if (ChunkAPI.isChunkClaimed(player.getLocation().getChunk())){
                player.sendMessage(Main.getPrefix() + "§cThis chunk is already claimed");
                return false;
            }

            ParticleManager.claim(player);
            ChunkAPI.claimChunk(player, player.getLocation().getChunk());
            player.sendMessage(Main.getPrefix() + "§7Successfully claimed this chunk it is marked with a §agreen §7border");
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 100);

        } else if (label.equalsIgnoreCase("unclaim")) {
            if (!ChunkAPI.isChunkClaimedByPlayer(player, player.getLocation().getChunk())){
                player.sendMessage(Main.getPrefix() + "§cThis chunk is not claimed by you to claim it execute §6/claim");
                return false;
            }

            if (args.length == 0){
                player.sendMessage(Main.getPrefix() + "§cAre you sure you want to unclaim this chunk to confirm execute §6/unclaim confirm");
                confirming.add(player);
                return false;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("confirm") || args.length == 1 && args[0].equalsIgnoreCase("c")){
                if (!confirming.contains(player)){
                    player.sendMessage(Main.getPrefix() + "§cYou cannot confirm unclaim instantly type §6/unclaim §cfirst");
                    return false;
                }

                ChunkAPI.unclaimChunk(player, player.getLocation().getChunk());
                player.sendMessage(Main.getPrefix() + "§7Chunk successfully unclaimed the unclaimed chunk is marked with a §cred §7border");
                ParticleManager.unclaim(player);

                confirming.remove(player);
            }
        } else if (label.equalsIgnoreCase("unclaimall")) {
            if (ChunkAPI.getAllPlayerChunks(player).isEmpty()){
                player.sendMessage(Main.getPrefix() + "§cYou dont have any claimed chunks claim one by executing §6/claim §cwhile standing in the chunk you want to claim");
                return false;
            }

            if (!ChunkAPI.isChunkClaimedByPlayer(player, player.getLocation().getChunk())){
                player.sendMessage(Main.getPrefix() + "§cYou have to be in one of your claimed chunks to unclaim all at once");
                ParticleManager.displayAllClaimedChunks(player);
                return false;
            }

            if (args.length == 0){
                player.sendMessage(Main.getPrefix() + "§cARE YOU SURE YOU WANT TO UNCLAIM ALL YOUR CHUNKS IN ALL WORLDS? TO CONFIRM EXECUTE §6/unclaim confirm");
                return false;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("confirm") || args.length == 1 && args[0].equalsIgnoreCase("c")){
                if (!confirming.contains(player)){
                    player.sendMessage(Main.getPrefix() + "§cYou cannot confirm unclaimall instantly type §6/unclaimall first");
                    confirming.add(player);
                    return false;
                }

                ParticleManager.unclaimAll(player);
                ChunkAPI.unclaimAll(player);
                player.sendMessage(Main.getPrefix() + "§7Successfully unclaimed all your chunks they are marked with a §cred §7border");

                confirming.remove(player);
            }
        } else if (label.equalsIgnoreCase("claims")){
            if (ChunkAPI.getAllPlayerChunks(player).isEmpty()){
                player.sendMessage(Main.getPrefix() + "§cYou dont have any claimed chunks claim one by executing §6/claim §cwhile standing in the chunk you want to claim");
                return false;
            }

            if (args.length == 0){
                player.sendMessage(Main.getPrefix() + "§7All your claimed chunks are now displayed with a §fwhite §7border");
                ParticleManager.displayAllClaimedChunks(player);
            }
        }

        return false;
    }

}