/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit;

import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import me.hamzaxx.fperms.bukkit.data.GroupData;
import me.hamzaxx.fperms.bukkit.data.PlayerData;
import me.hamzaxx.fperms.bukkit.listeners.JoinListener;
import me.hamzaxx.fperms.bukkit.listeners.LeaveListener;
import me.hamzaxx.fperms.bukkit.netty.ClientHandler;
import me.hamzaxx.fperms.bukkit.permissions.PermissionsInjector;
import me.hamzaxx.fperms.bukkit.permissions.fPermsPermissible;
import me.hamzaxx.fperms.bukkit.vault.ChatCompatibility;
import me.hamzaxx.fperms.bukkit.vault.PermissionsCompatibility;
import me.hamzaxx.fperms.common.netty.ClientBye;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class fPermsPlugin extends JavaPlugin
{
    private Map<String, PlayerData> playerCache = new HashMap<>();
    private Map<String, GroupData> groupCache = new HashMap<>();

    private Config config;
    private Gson gson = new Gson();
    private Channel channel;
    private EventLoopGroup group;

    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        config = new Config( this );
        Plugin vault = Bukkit.getPluginManager().getPlugin( "Vault" );
        if ( vault != null && vault.isEnabled() )
        {
            Bukkit.getServicesManager().register( Chat.class, new ChatCompatibility( this,
                    new PermissionsCompatibility( this ) ), this, ServicePriority.Highest );
            getLogger().info( "Hooked into vault!" );
        }
        registerEvents();
        setupClient();
        Bukkit.getOnlinePlayers().forEach( player ->
                new PermissionsInjector( player, new fPermsPermissible( player, this ) ).inject() );
    }

    @Override
    public void onDisable()
    {
        if ( channel.isActive() )
            kill();
    }

    private void registerEvents()
    {
        Stream.of( new JoinListener( this ), new LeaveListener( this ) ).forEach( listener ->
                getServer().getPluginManager().registerEvents( listener, this ) );
    }

    public void kill()
    {
        try
        {
            if ( channel != null )
            {
                getChannel().writeAndFlush( new String[]{ "clientBye", getGson().toJson(
                        new ClientBye( getConfiguration().getServerName() ) ) } ).await();
            }
        } catch ( InterruptedException e )
        {
            e.printStackTrace();
        } finally
        {
            if ( channel != null )
            {
                channel.closeFuture();
                channel.close();
            }
            group.shutdownGracefully();
        }
    }

    private synchronized void setupClient()
    {
        group = new NioEventLoopGroup();
        try
        {
            ClientHandler clientHandler = new ClientHandler( this );
            Bootstrap b = new Bootstrap();
            b.group( group )
                    .channel( NioSocketChannel.class )
                    .handler( new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel socketChannel) throws Exception
                        {
                            socketChannel.pipeline().addLast( new ObjectDecoder(
                                            ClassResolvers.cacheDisabled( ClassLoader.getSystemClassLoader() ) ),
                                    new ObjectEncoder(), clientHandler );
                        }

                    } );
            channel = b.connect( getConfiguration().getServerAddress() ).sync().channel();
        } catch ( InterruptedException e )
        {
            kill();
            Bukkit.shutdown();
        }
    }

    public Config getConfiguration()
    {
        return config;
    }

    public Map<String, GroupData> getGroups()
    {
        return groupCache;
    }

    public Map<String, PlayerData> getPlayerData()
    {
        return playerCache;
    }

    public Channel getChannel()
    {
        return channel;
    }

    public Gson getGson()
    {
        return gson;
    }
}