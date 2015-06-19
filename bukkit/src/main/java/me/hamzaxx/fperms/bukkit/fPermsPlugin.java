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
import me.hamzaxx.fperms.shared.data.GroupData;
import me.hamzaxx.fperms.shared.data.PlayerData;
import me.hamzaxx.fperms.bukkit.listeners.CommandListener;
import me.hamzaxx.fperms.bukkit.listeners.JoinListener;
import me.hamzaxx.fperms.bukkit.listeners.LeaveListener;
import me.hamzaxx.fperms.bukkit.netty.ClientHandler;
import me.hamzaxx.fperms.bukkit.permissions.PermissionsInjector;
import me.hamzaxx.fperms.bukkit.permissions.fPermsPermissible;
import me.hamzaxx.fperms.bukkit.vault.ChatCompatibility;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class fPermsPlugin extends JavaPlugin
{
    private Map<String, PlayerData> playerCache = new HashMap<>();
    private Map<String, GroupData> groupCache = new HashMap<>();

    private static fPermsPlugin plugin;
    private static Gson gson = new Gson();
    private Channel channel;
    private EventLoopGroup group;

    @Override
    public void onEnable()
    {

        saveDefaultConfig();
        plugin = this;
        //getServer().getMessenger().registerOutgoingPluginChannel( this, "fPerms" );
        //getServer().getMessenger().registerIncomingPluginChannel( this, "fPermsPlugin", new MessageListener() );
        Bukkit.getServicesManager().register( Chat.class, new ChatCompatibility( this ), this, ServicePriority.Highest );
        registerEvents();
        setupClient();
        handleTempData();
        Bukkit.getOnlinePlayers().forEach( player ->
                new PermissionsInjector( player, new fPermsPermissible( player, this ) ).inject() );
    }

    private void handleTempData()
    {
        File dataFile = new File( getDataFolder().getPath() + File.separator + "temp.dat" );
        if ( dataFile.exists() )
        {
            if ( dataFile.length() != 0 )
            {
                try ( ObjectInputStream in = new ObjectInputStream( new FileInputStream( dataFile ) ) )
                {
                    @SuppressWarnings("unchecked")
                    Map<String, PlayerData> mapPersisted = ( HashMap<String, PlayerData> ) in.readObject();
                    playerCache = mapPersisted;
                    new PrintWriter( dataFile ).close();
                } catch ( ClassNotFoundException | IOException e )
                {
                    e.printStackTrace();
                }
                getLogger().info( "Data persisted!" );
            }
        } else
        {
            try
            {
                if ( dataFile.createNewFile() )
                {
                    getLogger().info( "Saved temp data file!" );
                }
            } catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable()
    {
        kill();
        //Permissions.clearPermissions();
        plugin = null;
    }

    private void registerEvents()
    {
        for ( Listener listener : new Listener[]{
                new JoinListener( this ), new LeaveListener( this ),
                new CommandListener( this ) } )
        {
            getServer().getPluginManager().registerEvents( listener, this );
        }
    }

    public void kill()
    {
        channel.closeFuture();
        channel.close();
        group.shutdownGracefully();
    }

    private void setupClient()
    {
        group = new NioEventLoopGroup();
        try
        {
            Bootstrap b = new Bootstrap();
            b.group( group )
                    .channel( NioSocketChannel.class )
                    .handler( new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception
                        {
                            socketChannel.pipeline().addLast( new ObjectDecoder( ClassResolvers.cacheDisabled( null ) ), new ObjectEncoder(),
                                    // TODO: make configurable
                                    new ClientHandler( "hub", plugin ) );
                        }

                    } );

            // TODO: make configurable
            channel = b.connect( "0.0.0.0", 6969 ).sync().channel();
        } catch ( InterruptedException e )
        {
            kill();
            Bukkit.getPluginManager().disablePlugin( this );
        }
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

    public static fPermsPlugin getInstance()
    {
        return plugin;
    }

    public Gson getGson()
    {
        return gson;
    }
}
