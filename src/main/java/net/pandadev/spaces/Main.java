package net.pandadev.spaces;

import net.pandadev.spaces.commands.ClaimCommand;
import net.pandadev.spaces.listeners.ProtectListener;
import net.pandadev.spaces.utils.Configs;
import net.pandadev.spaces.utils.ParticleManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static final String Prefix = "§f§lSpaces §8» ";

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        Configs.saveChunksConfig();

        registerListeners();
        registerCommands();

        Bukkit.getConsoleSender().sendMessage(Prefix + "§aActivated");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Prefix + "§cDeactivated");

        instance = null;
    }

    private void registerCommands() {
        getCommand("claim").setExecutor(new ClaimCommand());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ParticleManager(), this);
        Bukkit.getPluginManager().registerEvents(new ProtectListener(), this);
    }


    public static String getPrefix() {
        return Prefix;
    }

    public static Main getInstance() {
        return instance;
    }
}

