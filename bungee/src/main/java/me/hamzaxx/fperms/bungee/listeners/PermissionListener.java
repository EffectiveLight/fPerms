package me.hamzaxx.fperms.bungee.listeners;

import me.hamzaxx.fperms.bungee.data.Data;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import me.hamzaxx.fperms.common.permissions.Permission;
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
            Data playerData = plugin.getDataSource().getPlayerData( player.getUniqueId() );
            Map<String, Permission> permissions = playerData.getEffectiveBungeePermissions();

            if ( permissions.containsKey( "*" ) )
            {
                Permission permission = permissions.get( "*" );
                switch ( permission.getLocationType() )
                {
                    case ALL:
                        event.setHasPermission( permission.getValue() );
                        break;
                    case SERVER:
                        event.setHasPermission( permission.getValue()
                                && player.getServer().getInfo().getName().equals(
                                permission.getLocationName() ) );
                        break;
                    default:
                        event.setHasPermission( false );
                        break;
                }
            }

            if ( permissions.containsKey( event.getPermission() ) )
            {
                Permission permission = permissions.get( event.getPermission() );
                switch ( permission.getLocationType() )
                {
                    case ALL:
                        event.setHasPermission( permission.getValue() );
                        break;
                    case SERVER:
                        event.setHasPermission( permission.getValue()
                                && player.getServer().getInfo().getName().equals(
                                permission.getLocationName() ) );
                        break;
                    default:
                        event.setHasPermission( false );
                        break;
                }
            }
        } else
        {
            event.setHasPermission( true );
        }
    }
}
