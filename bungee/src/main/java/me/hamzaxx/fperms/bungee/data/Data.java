/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data;

import me.hamzaxx.fperms.common.permissions.Permission;

import java.util.Map;

public interface Data
{

    String getGroupName();

    String getPrefix();

    String getSuffix();

    Map<String, Permission> getBukkitPermissions();

    Map<String, Permission> getBungeePermissions();

    Map<String, Permission> getEffectiveBungeePermissions();

    Map<String, Permission> getEffectiveBukkitPermissions();

    boolean unsetBungeePermission(String permission);

    boolean unsetBukkitPermission(String permission);

    void setBungeePermission(Permission permission);

    void setBukkitPermission(Permission permission);

    void setPrefix(String prefix);

    void setSuffix(String suffix);
}
