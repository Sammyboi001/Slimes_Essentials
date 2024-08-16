package org.slimeovsky;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupCommandExecutor implements CommandExecutor {

    private final GroupManager groupManager;

    public GroupCommandExecutor(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            sender.sendMessage("Usage: /group <create|add|addp|addprefix> ...");
            return false;
        }

        String subCommand = args[0];

        switch (subCommand.toLowerCase()) {
            case "create":
                if (args.length != 2) {
                    sender.sendMessage("Usage: /group create <group>");
                    return false;
                }
                groupManager.createGroup(args[1]);
                sender.sendMessage("Group created: " + args[1]);
                break;

            case "add":
                if (args.length != 3) {
                    sender.sendMessage("Usage: /group add <player> <group>");
                    return false;
                }
                Player targetPlayer = sender.getServer().getPlayer(args[1]);
                if (targetPlayer == null) {
                    sender.sendMessage("Player not found.");
                    return false;
                }
                if (groupManager.addPlayerToGroup(targetPlayer, args[2])) {
                    sender.sendMessage("Player " + args[1] + " added to group " + args[2]);
                } else {
                    sender.sendMessage("Group not found.");
                }
                break;

            case "addp":
                if (args.length != 3) {
                    sender.sendMessage("Usage: /group <group> addp <permission>");
                    return false;
                }
                if (groupManager.addPermissionToGroup(args[1], args[2])) {
                    sender.sendMessage("Permission added to group " + args[1]);
                } else {
                    sender.sendMessage("Group not found.");
                }
                break;

            case "addprefix":
                if (args.length != 3) {
                    sender.sendMessage("Usage: /group <group> addprefix <prefix>");
                    return false;
                }
                if (groupManager.setPrefixForGroup(args[1], args[2])) {
                    sender.sendMessage("Prefix set for group " + args[1]);
                } else {
                    sender.sendMessage("Group not found.");
                }
                break;

            default:
                sender.sendMessage("Unknown subcommand.");
                break;
        }

        return true;
    }
}
