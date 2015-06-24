/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import me.hamzaxx.fperms.common.netty.Change;
import me.hamzaxx.fperms.common.netty.ChangeType;
import me.hamzaxx.fperms.common.netty.ClientBye;
import me.hamzaxx.fperms.common.netty.ClientHello;
import me.hamzaxx.fperms.common.permissions.PermissionData;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String[]>
{
    private fPermsPlugin plugin;

    public ServerHandler(fPermsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        for ( Map.Entry<String, Channel> entry : plugin.getChannels().entrySet() )
        {
            if ( entry.getValue().equals( ctx.channel() ) )
            {
                plugin.getLogger().info( "Server " + entry.getKey() + " disconnected!" );
                plugin.getChannels().remove( entry.getKey() );
                ctx.close();
                break;
            }
        }

        if ( !( cause instanceof IOException )
                && !( cause instanceof InterruptedException ) )
        {
            cause.printStackTrace();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String[] msg) throws Exception
    {
        System.out.println( Arrays.toString( msg ) );
        switch ( msg[ 0 ] )
        {
            case "clientHello":
                ClientHello hello = plugin.getGson().fromJson( msg[ 1 ], ClientHello.class );
                System.out.println( hello.getServerName() + " connected." );
                plugin.getChannels().put( hello.getServerName(), ctx.channel() );
                plugin.getDataSource().getGroups().values().forEach( groupData -> {
                    PermissionData data = new PermissionData( groupData.getGroupName(),
                            groupData.getPrefix(), groupData.getSuffix(), groupData.getEffectiveBukkitPermissions() );
                    plugin.getLogger().info( groupData.getGroupName() );
                    ctx.writeAndFlush( new String[]{ "change", plugin.getGson().toJson(
                            new Change( ChangeType.GROUP, groupData.getGroupName() ) ), plugin.getGson().toJson( data ) } );
                } );
                break;
            case "clientBye":
                ClientBye bye = plugin.getGson().fromJson( msg[ 1 ], ClientBye.class );
                plugin.getChannels().remove( bye.getServerName() );
                plugin.getLogger().info( bye.getServerName() + " disconnected." );
                ctx.close();
                break;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        for ( Map.Entry<String, Channel> entry : plugin.getChannels().entrySet() )
        {
            if ( entry.getValue().equals( ctx.channel() ) )
            {
                plugin.getLogger().info( "Server " + entry.getKey() + " disconnected!" );
                plugin.getChannels().remove( entry.getKey() );
                break;
            }
        }
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }
}
