/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.common.permissions;

import java.util.Collection;

public class PermissionData
{

    private String groupName;
    private String prefix;
    private String suffix;
    private Collection<Permission> permissions;

    public PermissionData(String groupName, String prefix,
                          String suffix, Collection<Permission> permissions)
    {
        this.groupName = groupName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.permissions = permissions;
    }

    public String getName()
    {
        return groupName;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getSuffix()
    {
        return suffix;
    }

    public Collection<Permission> getPermissions()
    {
        return permissions;
    }
}
