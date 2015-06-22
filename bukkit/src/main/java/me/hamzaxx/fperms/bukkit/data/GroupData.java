/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.data;

import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import me.hamzaxx.fperms.shared.permissions.Permission;
import me.hamzaxx.fperms.shared.permissions.PermissionData;

import java.util.Map;

public class GroupData implements Data
{

    private fPermsPlugin plugin;
    private String name;
    private String prefix;
    private String suffix;
    private Map<String, Permission> permissions;

    public GroupData(fPermsPlugin plugin, String name, String prefix, String suffix, Map<String, Permission> permissions)
    {
        this.plugin = plugin;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.permissions = permissions;
    }

    public GroupData(fPermsPlugin plugin, PermissionData data)
    {
        this.plugin = plugin;
        this.name = data.getName();
        this.prefix = data.getPrefix();
        this.suffix = data.getSuffix();
        this.permissions = data.getPermissions();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getPrefix()
    {
        return prefix;
    }

    @Override
    public String getSuffix()
    {
        return suffix;
    }

    @Override
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    @Override
    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
    }

    @Override
    public void setPermission(Permission permission)
    {
        permissions.put( permission.getName(), permission );
        plugin.getPlayerData().values().stream().filter( playerData ->
                getName().equals( playerData.getName() ) ).forEach( playerData ->
                playerData.setPermission( permission ) );
    }

    @Override
    public void unsetPermission(String permission)
    {
        permissions.remove( permission );
        plugin.getPlayerData().values().stream().filter( playerData ->
                getName().equals( playerData.getName() ) ).forEach( PlayerData::recalculatePermissions );
    }

    public void setPermissions(Map<String, Permission> permissions)
    {
        this.permissions = permissions;
        plugin.getPlayerData().values().stream().filter( playerData ->
                getName().equals( playerData.getName() ) ).forEach( PlayerData::recalculatePermissions );
    }

    @Override
    public Map<String, Permission> getPermissions()
    {
        return permissions;
    }
}
