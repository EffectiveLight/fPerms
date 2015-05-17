/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.hamzaxx.fperms.bungee.fPermsPlugin;

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
        ctx.write( "hi" );
        ctx.flush();
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
        if ( object instanceof String )
        {
            System.out.println( (String) object );
            String[] data = ( (String) object ).split( "\\|" );
            switch ( data[ 0 ] )
            {
                case "server":
                    plugin.getChannels().put( data[ 1 ], channelHandlerContext.channel() );
                    System.out.println( data[ 1 ] + " connected!" );
                    break;
                case "bye":
                    plugin.getChannels().remove( data[ 1 ] );
                    break;
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }
}
