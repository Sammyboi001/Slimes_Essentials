package org.slimeovsky;

import java.util.HashSet;
import java.util.Set;

public class Group {

    private final String name;
    private final Set<String> permissions = new HashSet<>();
    private String prefix = "";

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
