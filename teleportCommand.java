package org.slimeovsky;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class teleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED+"Usage: /teleport <player|x> <y> <z>");
            return false;
        }

        if (args.length == 1) {
            // Teleport to another player
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                player.sendMessage(ChatColor.RED+"Player not found.");
                return false;
            }
            player.teleport(target.getLocation());
            player.sendMessage(ChatColor.GREEN+"Teleported to player " + target.getName() + ".");
        } else if (args.length == 3) {
            try {
                double x = Double.parseDouble(args[0]);
                double y = Double.parseDouble(args[1]);
                double z = Double.parseDouble(args[2]);

                Location location = new Location(player.getWorld(), x, y, z);
                player.teleport(location);
                player.sendMessage(ChatColor.GREEN+"Teleported to coordinates (" + x + ", " + y + ", " + z + ").");
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED+"Invalid coordinates.");
                return false;
            }
        } else {
            player.sendMessage(ChatColor.RED+"Usage: /teleport <player|x> <y> <z>");
            return false;
        }

        return true;
    }
}
