/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.hamzaxx.fperms.bukkit.Permissions;
import me.hamzaxx.fperms.bukkit.data.PlayerData;
import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends SimpleChannelInboundHandler<String>
{

    private fPermsPlugin plugin;

    public ClientHandler(fPermsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        Bukkit.getLogger().severe( "Lost connection to BungeeCord, disabling plugin!" );
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Bukkit.getPluginManager().disablePlugin( fPermsPlugin.getInstance() );
            }
        }.runTask( fPermsPlugin.getInstance() );
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        ctx.writeAndFlush( "server|hub" );
        ctx.writeAndFlush( "dsadsa" );
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        ctx.writeAndFlush( "bye|hub" );
    }

    @Override
    protected void channelRead0(ChannelHandlerContext chx, String msg) throws Exception
    {
        String[] data = msg.split( "\\|" );
        switch ( data[ 0 ] )
        {
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
        }
        System.out.println( msg );
    }
}
