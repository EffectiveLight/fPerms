/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.data;



import me.hamzaxx.fperms.shared.permissions.Permission;

import java.util.Map;

public interface Data
{

    String getName();

    String getPrefix();

    String getSuffix();

    void setPrefix(String prefix);

    void setSuffix(String suffix);

    void setPermission(Permission permission);

    void unsetPermission(String permission);

    void setPermissions(Map<String, Permission> permissions);

    Map<String, Permission> getPermissions();
}