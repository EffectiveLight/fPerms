/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.listeners;

import me.hamzaxx.fperms.bungee.data.PlayerData;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import me.hamzaxx.fperms.shared.netty.Change;
import me.hamzaxx.fperms.shared.netty.ChangeType;
import me.hamzaxx.fperms.shared.permissions.PermissionData;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerListener implements Listener
{

    private fPermsPlugin plugin;

    public ServerListener(fPermsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerConnect(ServerConnectEvent event)
    {
        if ( event.isCancelled() ) return;
        PlayerData playerData = plugin.getDataSource().getPlayerData( event.getPlayer().getUniqueId() );
        plugin.sendToServer( event.getPlayer().getServer(),
                new Change( ChangeType.PLAYER, event.getPlayer().getName(), plugin.getGson().toJson( new PermissionData(
                                playerData.getGroupName(), playerData.getPrefix(), playerData.getSuffix(), playerData.getEffectiveBukkitPermissions() ) ) ) );
    }
}
