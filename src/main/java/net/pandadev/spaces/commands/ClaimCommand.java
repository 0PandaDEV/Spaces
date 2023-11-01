package net.pandadev.spaces.commands;

import net.pandadev.spaces.Main;
import net.pandadev.spaces.utils.ChunkAPI;
import net.pandadev.spaces.utils.ParticleManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimCommand implements CommandExecutor {

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
            player.sendMessage(Main.getPrefix() + "§7Successfully claimed this chunk it is marked with a §x§f§f§0§0§0§0r§x§f§f§7§f§0§0a§x§f§f§f§f§0§0i§x§0§0§f§f§0§0n§x§0§0§0§0§f§fb§x§4§b§0§0§8§2o§x§9§4§0§0§d§3w §7border");

        } else if (label.equalsIgnoreCase("unclaim")) {
            if (!ChunkAPI.isChunkClaimedByPlayer(player, player.getLocation().getChunk())){
                player.sendMessage(Main.getPrefix() + "§cThis chunk is not claimed by you to claim it execute §6/claim");
                return false;
            }

            if (args.length == 0){
                player.sendMessage(Main.getPrefix() + "§cAre you sure you want to unclaim this chunk to confirm execute §6/unclaim confirm");
                return false;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("confirm") || args.length == 1 && args[0].equalsIgnoreCase("c")){
                ChunkAPI.unclaimChunk(player, player.getLocation().getChunk());
                player.sendMessage(Main.getPrefix() + "§7Chunk successfully unclaimed the unclaimed chunk is marked with a §cred §7border");
                ParticleManager.unclaim(player);
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
                player.sendMessage(Main.getPrefix() + "§cARE YOU SURE YOU WANT TO UNCLAIM ALL YOUR CHUNK TO CONFIRM EXECUTE §6/unclaim confirm");
                return false;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("confirm") || args.length == 1 && args[0].equalsIgnoreCase("c")){
                ParticleManager.unclaimAll(player);
                ChunkAPI.unclaimAll(player);
                player.sendMessage(Main.getPrefix() + "§7Successfully unclaimed all your chunks they are marked with a §cred §7border");
            }
        }

        return false;
    }

}