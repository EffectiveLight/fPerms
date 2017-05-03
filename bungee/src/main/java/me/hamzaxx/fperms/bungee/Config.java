package me.hamzaxx.fperms.bungee;

import net.md_5.bungee.Util;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Config
{

    private InetSocketAddress address;
    private int port;
    private String defaultGroupName;

    public Config(fPermsPlugin plugin)
    {
        try
        {
            Configuration config = ConfigurationProvider.getProvider(
                    YamlConfiguration.class ).load( new File( plugin.getDataFolder(), "bungeeconfig.yml" ) );
            port = config.getInt( "server-port" );
            defaultGroupName = config.getString( "default-group-name" );
            try
            {
                address = Util.getAddr( config.getString( "redis.address" ) );
            } catch ( NumberFormatException e )
            {
                plugin.getLogger().severe( "Incorrect server address given. Proxy shutting down." );
                plugin.getProxy().stop();
            }
        } catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public InetSocketAddress getAddress()
    {
        return address;
    }

    public int getPort()
    {
        return port;
    }

    public String getDefaultGroupName()
    {
        return defaultGroupName;
    }
}
