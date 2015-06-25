/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.net.InetSocketAddress;

public class Config
{
    private String serverName;
    private InetSocketAddress serverAddress;
    private String fastJoinMessage;

    public Config(Plugin plugin)
    {
        serverName = plugin.getConfig().getString( "server-name" );
        String[] address = plugin.getConfig().getString( "server-address" ).split( ":" );
        try
        {
            serverAddress = new InetSocketAddress( address[ 0 ], Integer.parseInt( address[ 1 ] ) );
        } catch ( NumberFormatException e )
        {
            plugin.getLogger().severe( "Valid server address wasn't provided, shutting down server." );
            Bukkit.shutdown();
        }
        fastJoinMessage = ChatColor.translateAlternateColorCodes( '&', plugin.getConfig().getString( "messages.fast-join" ) );
    }

    public String getServerName()
    {
        return serverName;
    }

    public InetSocketAddress getServerAddress()
    {
        return serverAddress;
    }

    public String getFastJoinMessage()
    {
        return fastJoinMessage;
    }
}
