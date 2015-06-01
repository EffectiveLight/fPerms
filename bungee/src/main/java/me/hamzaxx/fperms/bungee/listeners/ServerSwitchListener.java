/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.listeners;

import me.hamzaxx.fperms.bungee.data.Data;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitchListener implements Listener
{

    private fPermsPlugin plugin;

    public ServerSwitchListener(fPermsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event)
    {
        System.out.println( "Called Server Switch " + event.toString() );
        Data group = plugin.getDataSource().getPlayerGroup( event.getPlayer().getUniqueId() );
        Data player = plugin.getDataSource().getPlayerData( event.getPlayer().getUniqueId() );
        plugin.getChannels().get( event.getPlayer().getServer().getInfo().getName() ).writeAndFlush(
                String.format( "sendPerms|%s|%s|%s|%s|%s|%s|%s", event.getPlayer().getName(), group.getGroupName(),
                        group.getPrefix(), group.getSuffix(), player.getPrefix(), player.getSuffix(), player
                                .getEffectiveBukkitPermissionsJson() )
        );
        /*try ( ByteArrayOutputStream b = new ByteArrayOutputStream();
              DataOutputStream out = new DataOutputStream( b ) )
        {
            Data group = fPermsPlugin.getDataSource().getPlayerGroup( event.getPlayer().getUniqueId() );
            Data player = fPermsPlugin.getDataSource().getPlayerData( event.getPlayer().getUniqueId() );
            out.writeUTF( "sendPerms" );
            out.writeUTF( event.getPlayer().getName() );
            out.writeUTF( group.getGroupName() );
            out.writeUTF( group.getPrefix() );
            //event.getPlayer().getServer().getInfo().ge;
            out.writeUTF( group.getSuffix() );
            out.writeUTF( player.getEffectiveBukkitPermissionsJson() );
            event.getPlayer().getServer().sendData( "fPermsPlugin", b.toByteArray() );
        } catch ( IOException e )
        {
            ProxyServer.getInstance().getLogger().severe( e.getMessage() );
        }*/

    }


}
