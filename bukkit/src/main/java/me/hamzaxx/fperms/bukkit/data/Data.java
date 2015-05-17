/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.data;

import java.util.Map;

public interface Data
{

    String getGroupName();

    String getPlayerPrefix();

    String getPlayerSuffix();

    String getGroupPrefix();

    String getGroupSuffix();

    void setGroupPrefix(String prefix);

    void setGroupSuffix(String suffix);

    void setGroupName(String groupName);

    void setPlayerPrefix(String prefix);

    void setPlayerSuffix(String suffix);

    void setPermission(String permission, boolean value);

    void unsetPermission(String permission);

    Map<String, Boolean> getEffectivePermissions();
}