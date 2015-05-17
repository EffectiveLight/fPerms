/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public interface Data
{

    String getGroupName();

    String getPrefix();

    String getSuffix();

    ConcurrentMap<String, ConcurrentMap<String, Boolean>> getBukkitPermissions();

    ConcurrentMap<String, ConcurrentMap<String, Boolean>> getBungeePermissions();

    ConcurrentMap<String, ConcurrentMap<String, Boolean>> getEffectiveBungeePermissions();

    ConcurrentMap<String, ConcurrentMap<String, Boolean>> getEffectiveBukkitPermissions();

    String getEffectiveBukkitPermissionsJson();

    List<String> getParents();

    boolean unsetBungeePermission(String location, String permission);

    boolean unsetBukkitPermission(String location, String permission);

    void setBungeePermission(String location, String permission, boolean value);

    void setBukkitPermission(String location, String permission, boolean value);

    void setPrefix(String prefix);

    void setSuffix(String suffix);

    void addParent(String parent);

    void addParents(List<String> parents);
}
