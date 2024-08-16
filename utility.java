package org.slimeovsky;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class utility {

    public void sendMessage(Player p, char code, String message){
        p.sendMessage(ChatColor.translateAlternateColorCodes(code, message));
    }

}
