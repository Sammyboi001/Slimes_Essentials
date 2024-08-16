package org.slimeovsky;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final GroupManager groupManager;

    public ChatListener(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String playerPrefix = groupManager.getPrefixForPlayer(event.getPlayer());
        String formattedPrefix = ChatColor.translateAlternateColorCodes('&', playerPrefix+" ");
        String formattedMessage = ChatColor.translateAlternateColorCodes('&', formattedPrefix + event.getPlayer().getName() + ": " + event.getMessage());
        event.setFormat(formattedMessage);
    }
}
