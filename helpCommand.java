package org.slimeovsky;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class helpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return false;
        }
        Player p = (Player) sender;
        if (args.length < 1){
            if(cmd.getName().equalsIgnoreCase("help")){
                p.sendMessage(ChatColor.GREEN+"---------------------");
                p.sendMessage(ChatColor.GREEN+"-------Commands------");
                p.sendMessage(ChatColor.GREEN+"/Help - This command!");
                p.sendMessage(ChatColor.GREEN+"/Plugins - Shows a list of Plugins!");
                p.sendMessage(ChatColor.GREEN+"/Teleport - Teleport to players or coordinates!");
                p.sendMessage(ChatColor.GREEN+"---------------------");

            }
        }



        return true;
    }
}
