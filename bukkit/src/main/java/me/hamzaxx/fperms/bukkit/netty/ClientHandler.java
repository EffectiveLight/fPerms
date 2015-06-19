/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import me.hamzaxx.fperms.shared.data.GroupData;
import me.hamzaxx.fperms.shared.data.PlayerData;
import me.hamzaxx.fperms.shared.netty.Change;
import me.hamzaxx.fperms.shared.netty.ClientBye;
import me.hamzaxx.fperms.shared.netty.ClientHello;
import me.hamzaxx.fperms.shared.netty.ServerBye;
import me.hamzaxx.fperms.shared.permissions.Permission;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
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
        plugin.getLogger().severe( "Lost connection to BungeeCord, disabling plugin!" );
        ctx.close();
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Bukkit.getPluginManager().disablePlugin( fPermsPlugin.getInstance() );
            }
        }.runTask( fPermsPlugin.getInstance() );
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        ctx.writeAndFlush( "hi " + serverName );
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
                plugin.getLogger().info( "fPerms will shutdown, Reason: " + bye.getReason() + ", disabling plugin!" );
                chx.close();
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        Bukkit.getPluginManager().disablePlugin( plugin );
                    }
                }.runTask( fPermsPlugin.getInstance() );
                break;
            case "permissionChange":
                Change change = plugin.getGson().fromJson( msg[ 1 ], Change.class );
                handlePermissionChange( change );
                break;
        }
        System.out.println( Arrays.toString( msg ) );
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }

    private void handlePermissionChange(Change change)
    {
        Permission permission;
        switch ( change.getChangeType() )
        {
            case PLAYER:
                PlayerData playerData = plugin.getGson().fromJson( change.getData(), PlayerData.class );
                plugin.getPlayerData().put( change.getName(), playerData );
                break;
            case GROUP:
                GroupData groupData = plugin.getGson().fromJson( change.getData(), GroupData.class );
                plugin.getGroups().put( groupData.getName(), groupData );
                break;
            case GROUP_PREFIX:
                plugin.getGroups().get( change.getName() ).setPrefix( change.getData() );
                System.out.println( plugin );
                break;
            case GROUP_SUFFIX:
                plugin.getGroups().get( change.getName() ).setSuffix( change.getData() );
                break;
            case PLAYER_PREFIX:
                plugin.getPlayerData().get( change.getName() ).setPrefix( change.getData() );
                break;
            case PLAYER_SUFFIX:
                plugin.getPlayerData().get( change.getName() ).setSuffix( change.getData() );
                break;
            case SET_PLAYER_PERMISSION:
                permission = plugin.getGson().fromJson( change.getData(), Permission.class );
                plugin.getPlayerData().get( change.getName() )
                        .getEffectivePermissions().put( permission.getName(), permission );
                break;
            case UNSET_PLAYER_PERMISSION:
                plugin.getPlayerData().get( change.getName() )
                        .getEffectivePermissions().remove( change.getData() );
                break;
            case RESET_PLAYER_PERMISSIONS:
                @SuppressWarnings("unchecked")
                Map<String, Permission> permissionPlayerMap = plugin.getGson().fromJson( change.getData(), Map.class );
                plugin.getPlayerData().get( change.getName() ).setPermissions( permissionPlayerMap );
                break;
            case SET_GROUP_PERMISSION:
                permission = plugin.getGson().fromJson( change.getData(), Permission.class );
                plugin.getGroups().get( change.getName() )
                        .getEffectivePermissions().put( permission.getName(), permission );
                updatePlayers();
                break;
            case UNSET_GROUP_PERMISSION:
                permission = plugin.getGson().fromJson( change.getData(), Permission.class );
                plugin.getGroups().get( change.getName() ).setPermission( permission );
                updatePlayers();
                break;
            case RESET_GROUP_PERMISSIONS:
                @SuppressWarnings("unchecked")
                Map<String, Permission> permissionGroupMap = plugin.getGson().fromJson( change.getData(), Map.class );
                plugin.getGroups().get( change.getName() ).setPermissions( permissionGroupMap );
                updatePlayers();
                break;
        }
    }

    private void updatePlayers()
    {
        Bukkit.getOnlinePlayers().forEach( player -> {
            Map<String, Permission> tempMap = new HashMap<>();
            PlayerData playerData = plugin.getPlayerData().get( player.getName() );
            tempMap.putAll( playerData.getEffectivePermissions() );
            tempMap.putAll( playerData.getGroup().getEffectivePermissions() );
            playerData.setPermissions( tempMap );
        } );
    }
}
