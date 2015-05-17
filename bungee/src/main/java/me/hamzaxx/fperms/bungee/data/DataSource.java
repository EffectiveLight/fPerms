/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public interface DataSource
{

    ConcurrentMap<String, GroupData> getGroups();

    ConcurrentMap<UUID, PlayerData> getPlayerCache();

    GroupData addGroup(String groupName);

    boolean groupExists(String groupName);

    GroupData getGroup(String groupName);

    GroupData updateGroup(String groupName);

    void saveGroup(GroupData groupData);

    void savePlayer(PlayerData playerData);

    boolean playerExists(UUID playerUUID);

    void setPlayerGroup(ProxiedPlayer player, String groupName);

    GroupData getPlayerGroup(UUID playerUUID);

    PlayerData getPlayerData(UUID playerUUID);
}
