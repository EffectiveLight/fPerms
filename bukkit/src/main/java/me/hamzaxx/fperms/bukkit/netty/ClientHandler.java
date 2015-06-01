/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import me.hamzaxx.fperms.shared.netty.ClientBye;
import me.hamzaxx.fperms.shared.netty.ClientHello;
import me.hamzaxx.fperms.shared.netty.PermissionChange;
import me.hamzaxx.fperms.shared.netty.ServerBye;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ClientHandler extends SimpleChannelInboundHandler<Object>
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
        plugin.getLogger().info( "ACTIVE" );
        ctx.writeAndFlush( "hi " + serverName );
        ctx.writeAndFlush( new ClientHello( serverName ) );
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        ctx.writeAndFlush( new ClientBye( serverName ) );
    }

    @Override
    protected void channelRead0(ChannelHandlerContext chx, Object object) throws Exception
    {
        //String[] data = msg.split( "\\|" );
        plugin.getLogger().info( object.getClass().getTypeName() );
        boolean b = false;
        if ( object instanceof ServerBye )
        {
            b = true;
            plugin.getLogger().info( "server BYE" );
            plugin.getLogger().info( "fPerms will shutdown, Reason: " +
                    ( ( ServerBye ) object ).getReason() + ", disabling plugin!" );
            chx.close();
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    Bukkit.getPluginManager().disablePlugin( plugin );
                }
            }.runTask( fPermsPlugin.getInstance() );
        } else if ( object instanceof PermissionChange )
        {
            b = true;
            plugin.getLogger().info( "server perms" );
            PermissionChange change = ( PermissionChange ) object;
            switch ( change.getChangeType() )
            {
                case GROUP_PREFIX:
                    System.out.println( ( ( String ) change.getData() ) );
                    break;
            }
        }
        plugin.getLogger().info( String.valueOf( b ) );
        /*switch ( clazz )
        {
            case ClientHello.class:

                break;
            case "bye":
                System.out.println( "BungeeCord was turned off, disabling plugin!" );
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
            case "sendPerms":
                //Permissions.getPlayerData().put( data[ 1 ], new PlayerData( data[ 2 ]) );
                break;
        }*/
        System.out.println( object.toString() );
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }
}
