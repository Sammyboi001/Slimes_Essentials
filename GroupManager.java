package org.slimeovsky;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GroupManager {

    private final Map<String, Group> groups = new HashMap<>();
    private final Map<Player, Group> playerGroups = new HashMap<>();
    private final File dataFolder;

    public GroupManager(JavaPlugin plugin) {
        this.dataFolder = new File(plugin.getDataFolder(), "players");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        loadGroups();
        createDefaultGroups(); // Create default groups if they don't exist
        loadPlayerGroups();
    }

    public Group getGroup(String groupName) {
        return groups.get(groupName);
    }

    public void createGroup(String groupName) {
        if (!groups.containsKey(groupName)) {
            groups.put(groupName, new Group(groupName));
            saveGroups();
        }
    }

    public boolean addPlayerToGroup(Player player, String groupName) {
        Group newGroup = getGroup(groupName);
        if (newGroup == null) return false;

        // Save the player to the new group
        playerGroups.put(player, newGroup);
        savePlayerGroup(player, groupName);

        // Update player's display name and tab list
        updatePlayerDisplay(player);

        return true;
    }

    public boolean addPermissionToGroup(String groupName, String permission) {
        Group group = getGroup(groupName);
        if (group == null) return false;

        group.addPermission(permission);
        saveGroups();
        return true;
    }

    public boolean setPrefixForGroup(String groupName, String prefix) {
        Group group = getGroup(groupName);
        if (group == null) return false;

        group.setPrefix(prefix);
        saveGroups();
        return true;
    }

    public String getPrefixForPlayer(Player player) {
        Group group = playerGroups.get(player);
        if (group == null) return "";

        return group.getPrefix();
    }

    public boolean hasPermission(Player player, String permission) {
        Group group = playerGroups.get(player);
        if (group == null) return false;

        return group.hasPermission(permission);
    }

    private void createDefaultGroups() {
        if (!groups.containsKey("default")) {
            Group defaultGroup = new Group("default");
            defaultGroup.setPrefix("&7[Default]");
            groups.put("default", defaultGroup);
        }

        if (!groups.containsKey("moderator")) {
            Group moderatorGroup = new Group("moderator");
            moderatorGroup.setPrefix("&8[&bModerator&8]");
            moderatorGroup.addPermission("chat.moderate");
            groups.put("moderator", moderatorGroup);
        }

        if (!groups.containsKey("admin")) {
            Group adminGroup = new Group("admin");
            adminGroup.setPrefix("&8[&4Admin&8]");
            adminGroup.addPermission("*"); // Full permissions
            groups.put("admin", adminGroup);
        }

        if (!groups.containsKey("owner")) {
            Group ownerGroup = new Group("owner");
            ownerGroup.setPrefix("&8[&cOwner&8]");
            ownerGroup.addPermission("*"); // Full permissions
            groups.put("owner", ownerGroup);
        }

        saveGroups();
    }

    private void saveGroups() {
        File groupFile = new File(dataFolder.getParentFile(), "groups.yml");
        FileConfiguration groupConfig = YamlConfiguration.loadConfiguration(groupFile);
        groupConfig.set("groups", null); // Clear old data
        for (Group group : groups.values()) {
            groupConfig.set("groups." + group.getName() + ".prefix", group.getPrefix());
            groupConfig.set("groups." + group.getName() + ".permissions", new ArrayList<>(group.getPermissions()));
        }
        try {
            groupConfig.save(groupFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGroups() {
        File groupFile = new File(dataFolder.getParentFile(), "groups.yml");
        if (!groupFile.exists()) {
            return; // No file to load from
        }

        FileConfiguration groupConfig = YamlConfiguration.loadConfiguration(groupFile);
        for (String key : groupConfig.getConfigurationSection("groups").getKeys(false)) {
            String prefix = groupConfig.getString("groups." + key + ".prefix", "");
            Set<String> permissions = new HashSet<>(groupConfig.getStringList("groups." + key + ".permissions"));

            Group group = new Group(key);
            group.setPrefix(prefix);
            for (String perm : permissions) {
                group.addPermission(perm);
            }
            groups.put(key, group);
        }
    }

    private void savePlayerGroup(Player player, String groupName) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        playerConfig.set("group", groupName);
        try {
            playerConfig.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPlayerGroups() {
        File[] files = dataFolder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".yml")) {
                FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
                String playerUUID = file.getName().replace(".yml", "");
                String groupName = playerConfig.getString("group");

                Player player = Bukkit.getPlayer(java.util.UUID.fromString(playerUUID));
                if (player != null && groups.containsKey(groupName)) {
                    playerGroups.put(player, groups.get(groupName));
                }
            }
        }
    }

    private void updatePlayerDisplay(Player player) {
        String prefix = getPrefixForPlayer(player);
        player.setDisplayName(ChatColor.translateAlternateColorCodes('&', prefix) + player.getName());
        player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', prefix) + player.getName());
    }
}
