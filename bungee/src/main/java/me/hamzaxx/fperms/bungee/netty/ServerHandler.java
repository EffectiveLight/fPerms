/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import me.hamzaxx.fperms.shared.netty.ClientBye;
import me.hamzaxx.fperms.shared.netty.ClientHello;

import java.util.Map;

public class ServerHandler extends SimpleChannelInboundHandler<Object>
{
    fPermsPlugin plugin;

    public ServerHandler(fPermsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx)
    {
        ctx.writeAndFlush( "hi" );
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
                break;
            }
        }
        // DEBUG
        cause.printStackTrace();

        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception
    {
        plugin.getLogger().info( object.getClass().getName() );
        System.out.println( object.toString() );
        boolean b = false;
        if ( object instanceof ClientHello )
        {
            b = true;
            plugin.getLogger().info( "Client HELLO" );
            ClientHello hello = ( ClientHello ) object;
            plugin.getChannels().put( hello.getServerName(), channelHandlerContext.channel() );
        } else if ( object instanceof ClientBye ) {
            b = true;
            plugin.getLogger().info( "Client BYE" );
            ClientBye bye = ( ClientBye ) object;
            plugin.getChannels().remove( bye.getServerName() );
        }
        plugin.getLogger().info( String.valueOf( b ) );
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }
}
