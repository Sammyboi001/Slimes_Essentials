package org.slimeovsky;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

public class PlayerJoinListener implements Listener {

    private final GroupManager groupManager;

    public PlayerJoinListener(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Assign the default group if the player is not already in a group
        Group currentGroup = groupManager.getGroup("default");
        if (groupManager.getPrefixForPlayer(player).isEmpty()) {
            if (currentGroup != null) {
                groupManager.addPlayerToGroup(player, "default");
            }
        } else {
            currentGroup = groupManager.getGroup(groupManager.getPrefixForPlayer(player));
            if (currentGroup != null) {
                groupManager.addPlayerToGroup(player, currentGroup.getName());
            }
        }

        updateTabList();
        updateChat(player);
    }

    private void updateTabList() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            String prefix = groupManager.getPrefixForPlayer(onlinePlayer);
            onlinePlayer.setPlayerListName(ChatColor.translateAlternateColorCodes('&', prefix) + onlinePlayer.getName());
        }
    }

    private void updateChat(Player player) {
        String prefix = groupManager.getPrefixForPlayer(player);
        player.setDisplayName(ChatColor.translateAlternateColorCodes('&', prefix) + player.getName());
    }
}
