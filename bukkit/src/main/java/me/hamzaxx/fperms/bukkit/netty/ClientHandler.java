/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.hamzaxx.fperms.bukkit.data.GroupData;
import me.hamzaxx.fperms.bukkit.data.PlayerData;
import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import me.hamzaxx.fperms.common.netty.Change;
import me.hamzaxx.fperms.common.netty.ClientBye;
import me.hamzaxx.fperms.common.netty.ClientHello;
import me.hamzaxx.fperms.common.netty.ServerBye;
import me.hamzaxx.fperms.common.permissions.Permission;
import me.hamzaxx.fperms.common.permissions.PermissionData;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class ClientHandler extends SimpleChannelInboundHandler<String[]>
{

    private String serverName;
    private fPermsPlugin plugin;

    public ClientHandler(String serverName, fPermsPlugin plugin)
    {
        this.serverName = serverName;
        this.plugin = plugin;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {

        if ( cause instanceof IOException
                || cause instanceof InterruptedException )
        {
            ctx.close();
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    Bukkit.shutdown();
                }
            }.runTask( plugin );
        } else
        {
            cause.printStackTrace();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        ctx.writeAndFlush( new String[]{ "clientHello",
                plugin.getGson().toJson( new ClientHello( serverName ) ) } );
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        ctx.writeAndFlush( new String[]{ "clientBye", plugin.getGson().toJson( new ClientBye( serverName ) ) } );
    }

    @Override
    protected void channelRead0(ChannelHandlerContext chx, String[] msg) throws Exception
    {
        switch ( msg[ 0 ] )
        {
            case "serverBye":
                ServerBye bye = plugin.getGson().fromJson( msg[ 1 ], ServerBye.class );
                plugin.getLogger().info( "The server will shutdown, Reason: " + bye.getReason() + ", Shutting down server!" );
                chx.close();
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        Bukkit.getOnlinePlayers().forEach( player -> player.kickPlayer( "The Server was shutdown, Reason: " + bye.getReason() ) );
                        Bukkit.shutdown();
                    }
                }.runTask( plugin );
                break;
            case "change":
                Change change = plugin.getGson().fromJson( msg[ 1 ], Change.class );
                handlePermissionChange( change, msg[ 2 ] );
                break;
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }

    private void handlePermissionChange(Change change, String data)
    {
        Permission permission;
        switch ( change.getChangeType() )
        {
            case PLAYER:
                PermissionData playerPermissionData = plugin.getGson().fromJson( data, PermissionData.class );
                plugin.getPlayerData().put( change.getName(), new PlayerData( plugin, playerPermissionData ) );
                break;
            case GROUP:
                PermissionData groupPermissionData = plugin.getGson().fromJson( data, PermissionData.class );
                plugin.getGroups().put( change.getName(), new GroupData( plugin, groupPermissionData ) );
                break;
            case GROUP_PREFIX:
                plugin.getGroups().get( change.getName() ).setPrefix( data );
                System.out.println( plugin );
                break;
            case GROUP_SUFFIX:
                plugin.getGroups().get( change.getName() ).setSuffix( data );
                break;
            case PLAYER_PREFIX:
                plugin.getPlayerData().get( change.getName() ).setPrefix( data );
                break;
            case PLAYER_SUFFIX:
                plugin.getPlayerData().get( change.getName() ).setSuffix( data );
                break;
            case SET_PLAYER_PERMISSION:
                permission = plugin.getGson().fromJson( data, Permission.class );
                plugin.getPlayerData().get( change.getName() ).setPermission( permission );
                break;
            case UNSET_PLAYER_PERMISSION:
                plugin.getPlayerData().get( change.getName() ).unsetPermission( data );
                break;
            case SET_GROUP_PERMISSION:
                permission = plugin.getGson().fromJson( data, Permission.class );
                plugin.getGroups().get( change.getName() ).setPermission( permission );
                break;
            case UNSET_GROUP_PERMISSION:
                plugin.getGroups().get( change.getName() ).unsetPermission( data );
                break;
            case REFRESH_PERMISSIONS:
                @SuppressWarnings("unchecked")
                Map<String, Permission> permissions = plugin.getGson().fromJson( data, Map.class );
                plugin.getGroups().get( change.getName() ).setPermissions( permissions );
                break;
            case GROUP_NAME:
                plugin.getPlayerData().get( change.getName() ).setGroup( data );
                break;
        }
    }
}
