/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.shared.data;

import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import me.hamzaxx.fperms.shared.permissions.Permission;

import java.io.Serializable;
import java.util.Map;

public class PlayerData implements Data, Serializable
{

    private fPermsPlugin plugin;
    private String groupName;
    private String prefix;
    private String suffix;

    private Map<String, Permission> permissions;

    public PlayerData(fPermsPlugin plugin, String groupName, String prefix,
                      String suffix, Map<String, Permission> permissions)
    {
        this.plugin = plugin;
        this.groupName = groupName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.permissions = permissions;
    }

    @Override
    public String getName()
    {
        return groupName;
    }

    public GroupData getGroup()
    {
        return plugin.getGroups().get( getName() );
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
    }

    @Override
    public void unsetPermission(String permission)
    {
        permissions.remove( permission );
    }


    @Override
    public Map<String, Permission> getEffectivePermissions()
    {
        return permissions;
    }
}
