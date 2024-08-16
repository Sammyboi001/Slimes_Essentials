package org.slimeovsky;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private GroupManager groupManager;

    @Override
    public void onEnable() {
        // Initialize GroupManager with this plugin instance
        groupManager = new GroupManager(this);

        // Register commands and listeners
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Handle any cleanup if necessary
    }

    private void registerCommands() {
        // Register commands with their respective executors
        this.getCommand("help").setExecutor(new helpCommand());
        this.getCommand("teleport").setExecutor(new teleportCommand());
        this.getCommand("group").setExecutor(new GroupCommandExecutor(groupManager));
    }

    private void registerListeners() {
        // Register event listeners
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(groupManager), this);
        this.getServer().getPluginManager().registerEvents(new ChatListener(groupManager), this);
    }
}
