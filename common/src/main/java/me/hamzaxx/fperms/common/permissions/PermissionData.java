package me.hamzaxx.fperms.common.permissions;

import java.util.Map;

public class PermissionData
{

    private String groupName;
    private String prefix;
    private String suffix;
    private Map<String, Permission> permissions;

    public PermissionData(String groupName, String prefix,
                          String suffix, Map<String, Permission> permissions)
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

    public Map<String, Permission> getPermissions()
    {
        return permissions;
    }
}
