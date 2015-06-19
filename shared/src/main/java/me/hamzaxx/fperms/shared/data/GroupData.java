/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.shared.data;

import me.hamzaxx.fperms.shared.permissions.Permission;

import java.util.Map;

public class GroupData implements Data
{

    private String name;
    private String prefix;
    private String suffix;
    private Map<String, Permission> permissions;

    public GroupData(String name, String prefix, String suffix, Map<String, Permission> permissions)
    {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.permissions = permissions;
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
    }

    @Override
    public void unsetPermission(String permission)
    {
        permissions.remove( permission );
    }

    public Map<String, Permission> getEffectivePermissions()
    {
        return permissions;
    }
}
