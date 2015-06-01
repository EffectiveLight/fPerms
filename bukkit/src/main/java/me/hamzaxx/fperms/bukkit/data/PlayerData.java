/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.data;

import me.hamzaxx.fperms.bukkit.fPermsPlugin;

import java.io.Serializable;
import java.util.Map;

public class PlayerData implements Data, Serializable
{

    private fPermsPlugin plugin;
    private String groupName;
    private String groupPrefix;
    private String groupSuffix;
    private String playerPrefix;
    private String playerSuffix;

    private Map<String, Boolean> permissions;

    public PlayerData(fPermsPlugin plugin, String groupName, String groupPrefix, String groupSuffix, String playerPrefix,
                      String playerSuffix, Map<String, Boolean> permissions)
    {
        this.plugin = plugin;
        this.groupName = groupName;
        this.groupPrefix = groupPrefix;
        this.groupSuffix = groupSuffix;
        this.playerPrefix = playerPrefix;
        this.playerSuffix = playerSuffix;
        this.permissions = permissions;
    }

    @Override
    public String getGroupName()
    {
        return groupName;
    }

    @Override
    public String getPlayerPrefix()
    {
        return playerPrefix;
    }

    @Override
    public String getPlayerSuffix()
    {
        return playerSuffix;
    }


    public String getGroupPrefix()
    {
        return groupPrefix;
    }

    @Override
    public String getGroupSuffix()
    {
        return groupSuffix;
    }

    @Override
    public void setGroupPrefix(String prefix)
    {
        this.groupPrefix = prefix;
    }

    @Override
    public void setGroupSuffix(String suffix)
    {
        this.groupSuffix = suffix;
    }

    @Override
    public Map<String, Boolean> getEffectivePermissions()
    {
        return permissions;
    }

    @Override
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    @Override
    public void setPlayerPrefix(String prefix)
    {
        this.playerPrefix = prefix;
    }

    @Override
    public void setPlayerSuffix(String suffix)
    {
        this.playerSuffix = suffix;
    }

    @Override
    public void setPermission(String perm, boolean value)
    {
        permissions.put( perm, value );
    }

    @Override
    public void unsetPermission(String perm)
    {
        permissions.remove( perm );
    }
}
