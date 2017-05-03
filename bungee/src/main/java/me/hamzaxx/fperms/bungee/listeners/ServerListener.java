package me.hamzaxx.fperms.bungee.listeners;

import me.hamzaxx.fperms.bungee.data.PlayerData;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import me.hamzaxx.fperms.common.netty.Change;
import me.hamzaxx.fperms.common.netty.ChangeType;
import me.hamzaxx.fperms.common.permissions.PermissionData;
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
        if ( event.isCancelled() || !plugin.getChannels().containsKey( event.getTarget().getName() ) ) return;
        PlayerData playerData = plugin.getDataSource().getPlayerData( event.getPlayer().getUniqueId() );
        plugin.sendToServer( event.getTarget(), new Change( ChangeType.PLAYER, event.getPlayer().getName(),
                plugin.getGson().toJson( new PermissionData( playerData.getGroupName(), playerData.getPrefix(),
                        playerData.getSuffix(), playerData.getBukkitPermissions() ) ) ) );
    }
}
