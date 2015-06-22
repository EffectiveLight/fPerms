/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data;

import me.hamzaxx.fperms.shared.permissions.Permission;

import java.util.List;
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
