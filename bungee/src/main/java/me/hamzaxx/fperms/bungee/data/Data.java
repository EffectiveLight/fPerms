/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data;

import me.hamzaxx.fperms.shared.permissions.Location;
import me.hamzaxx.fperms.shared.permissions.Permission;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public interface Data
{

    String getGroupName();

    String getPrefix();

    String getSuffix();

    ConcurrentMap<String, Permission> getBukkitPermissions();

    ConcurrentMap<String, Permission> getBungeePermissions();

    ConcurrentMap<String, Permission> getEffectiveBungeePermissions();

    ConcurrentMap<String, Permission> getEffectiveBukkitPermissions();

    //String getEffectiveBukkitPermissionsJson();

    List<String> getParents();

    boolean unsetBungeePermission(String permission);

    boolean unsetBukkitPermission(String permission);

    void setBungeePermission(Permission permission);

    void setBukkitPermission(Permission permission);

    void setPrefix(String prefix);

    void setSuffix(String suffix);

    void addParent(String parent);

    void addParents(List<String> parents);
}
