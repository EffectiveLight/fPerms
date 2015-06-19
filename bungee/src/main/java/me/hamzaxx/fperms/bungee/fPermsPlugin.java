/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import me.hamzaxx.fperms.bungee.commands.fPermsCommand;
import me.hamzaxx.fperms.bungee.data.DataSource;
import me.hamzaxx.fperms.bungee.listeners.PermissionListener;
import me.hamzaxx.fperms.bungee.listeners.ServerListener;
import me.hamzaxx.fperms.bungee.netty.ServerHandler;
import me.hamzaxx.fperms.shared.netty.ChangeType;
import me.hamzaxx.fperms.shared.netty.Change;
import me.hamzaxx.fperms.shared.netty.ServerBye;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class fPermsPlugin extends Plugin
{

    private static fPermsPlugin plugin;

    private static DataSource dataSource;

    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<>();

    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @Override
    public void onEnable()
    {
        plugin = this;
        getProxy().registerChannel( "fPerms" );
        getProxy().registerChannel( "fPermsPlugin" );
        getProxy().getPluginManager().registerCommand( this, new fPermsCommand( this ) );
        registerListeners();
        setupServer();
        getProxy().getScheduler().schedule( this, () -> getChannels().get( "hub" ).writeAndFlush(
                new Change( ChangeType.GROUP_PREFIX, "penis", name ) ), 15, TimeUnit.SECONDS );
        /*getProxy().getScheduler().schedule( this, () -> {
            System.out.println( "Sent message" );
            getChannels().get( "hub" ).writeAndFlush( "uuid|" + UUID.randomUUID().toString() );
        }, 25, TimeUnit.SECONDS );
        dataSource = new RedisDataSource( this );*/
    }

    @Override
    public void onDisable()
    {
        getChannels().values().forEach( channel ->
                channel.writeAndFlush( new ServerBye( "BungeeCord shutdown" ) ) );
        channel.closeFuture();
        channel.close();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        plugin = null;
    }

    private void registerListeners()
    {
        Stream.of( new ServerListener( this ), new PermissionListener( this ) ).forEach( listener ->
                getProxy().getPluginManager().registerListener( this, listener ) );

        /*for ( Listener listener : new Listener[]{ new LoginListener( this ), new MessageListener(),
                new PermissionListener( this ), new ServerSwitchListener( this ) } )
        {
            getProxy().getPluginManager().registerListener( this, listener );
        }*/
    }

    private void setupServer()
    {
        bossGroup = new NioEventLoopGroup( 1 );
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group( bossGroup, workerGroup )
                .channel( NioServerSocketChannel.class )
                        //.handler( new LoggingHandler( LogLevel.INFO ) )
                .childHandler( new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception
                    {
                        socketChannel.pipeline().addLast( new ObjectDecoder( ClassResolvers.cacheDisabled( null ) ), new ObjectEncoder(),
                                new ServerHandler( plugin ) );
                    }
                } );
        try
        {
            channel = b.bind( 6969 ).sync().channel();
        } catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
    }

    public ConcurrentMap<String, Channel> getChannels()
    {
        return channels;
    }

    public DataSource getDataSource()
    {
        return dataSource;
    }

    public Gson getGson()
    {
        return GSON;
    }

    public static fPermsPlugin getInstance()
    {
        return plugin;
    }
}
