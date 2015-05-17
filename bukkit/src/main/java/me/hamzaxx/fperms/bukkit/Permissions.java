/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit;

import me.hamzaxx.fperms.bukkit.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.Map;

import static me.hamzaxx.fperms.bukkit.fPermsPlugin.getInstance;

public class Permissions
{

    private static final Map<String, PermissionAttachment> PERMISSIONS = new HashMap<>();
    private static Map<String, PlayerData> playerData = new HashMap<>();

    public static void addPlayer(Player player)
    {
        PermissionAttachment attachment = player.addAttachment( getInstance() );
        PERMISSIONS.put( player.getName(), attachment );
    }

    public static void clearPermissions()
    {
        PERMISSIONS.values().forEach( org.bukkit.permissions.PermissionAttachment::remove );
        PERMISSIONS.clear();
        playerData.clear();
    }

    public static void removePlayer(Player player)
    {
        if ( PERMISSIONS.containsKey( player.getName() ) )
        {
            PermissionAttachment attachment = PERMISSIONS.get( player.getName() );
            player.removeAttachment( attachment );
            PERMISSIONS.remove( player.getUniqueId(), attachment );
            playerData.remove( player.getName() );
        }
    }

    public static void clearPlayer(Player player)
    {
        if ( PERMISSIONS.containsKey( player.getName() ) )
        {
            PermissionAttachment attachment = PERMISSIONS.get( player.getName() );
            attachment.getPermissions().clear();
        }
    }

    public static void setPermissions(Player player, Map<String, Boolean> map)
    {
        if ( PERMISSIONS.containsKey( player.getName() ) )
        {
            PermissionAttachment attachment = PERMISSIONS.get( player.getName() );
            for ( Map.Entry<String, Boolean> entry : map.entrySet() )
            {
                attachment.setPermission( entry.getKey(), entry.getValue() );
            }
        }
    }

    public static void setPermission(Player player, String perm, boolean value)
    {
        if ( PERMISSIONS.containsKey( player.getName() ) )
        {
            PermissionAttachment attachment = PERMISSIONS.get( player.getName() );
            attachment.setPermission( perm, value );
            playerData.get( player.getName() ).setPermission( perm, value );
        }
    }

    public static void unsetPermission(Player player, String perm)
    {
        if ( PERMISSIONS.containsKey( player.getName() ) )
        {
            PermissionAttachment attachment = PERMISSIONS.get( player.getName() );
            attachment.unsetPermission( perm );
            playerData.remove( perm );
        }
    }

    public static void setPlayerData(Map<String, PlayerData> playerData)
    {
        Permissions.playerData = playerData;
    }

    public static Map<String, PlayerData> getPlayerData()
    {
        return playerData;
    }
}
