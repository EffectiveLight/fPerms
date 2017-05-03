package me.hamzaxx.fperms.bungee.data.redis;

import me.hamzaxx.fperms.bungee.data.Data;
import me.hamzaxx.fperms.bungee.data.DataSource;
import me.hamzaxx.fperms.bungee.data.GroupData;
import me.hamzaxx.fperms.bungee.data.PlayerData;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class RedisDataSource implements DataSource
{

    private final fPermsPlugin plugin;
    private JedisPool pool;

    private final String PREFIX = "fPerms:";

    private ConcurrentMap<String, GroupData> groupCache = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, PlayerData> playerCache = new ConcurrentHashMap<>();

    public RedisDataSource(fPermsPlugin plugin)
    {
        this.plugin = plugin;
        ProxyServer.getInstance().getScheduler().runAsync( plugin, () ->
                pool = new JedisPool( new JedisPoolConfig(),
                        plugin.getConfig().getAddress().getHostName(),
                        plugin.getConfig().getAddress().getPort(), 0 ) );
        ProxyServer.getInstance().getScheduler().schedule( plugin, () -> {
            try
            {
                if ( !groupExists( plugin.getConfig().getDefaultGroupName() ) )
                    addGroup( plugin.getConfig().getDefaultGroupName() );
                updateGroups();
            } catch ( JedisConnectionException e )
            {
                plugin.getLogger().severe( "Couldn't connect to Redis, Proxy shutting down." );
                plugin.getProxy().stop();
            }
        }, 2, TimeUnit.SECONDS );
    }

    @Override
    public ConcurrentMap<UUID, PlayerData> getPlayerCache()
    {
        return playerCache;
    }

    @Override
    public ConcurrentMap<String, GroupData> getGroups()
    {
        return groupCache;
    }

    private String getJson(String groupName)
    {
        return get( PREFIX + groupName );
    }

    private void putJson(String groupName, String json)
    {
        put( PREFIX + groupName.toLowerCase(), json );
    }

    public void close()
    {
        pool.destroy();
    }

    @Override
    public void saveGroup(GroupData groupData)
    {
        String json = plugin.getExclusionaryGson().toJson( groupData );
        putJson( groupData.getGroupName(), json );
    }

    @Override
    public void savePlayer(PlayerData playerData)
    {
        String json = plugin.getExclusionaryGson().toJson( playerData );
        putJson( playerData.getPlayerUUID().toString(), json );
    }

    @Override
    public boolean groupExists(String groupName)
    {
        return groupCache.containsKey( groupName.toLowerCase() )
                || containsKey( PREFIX + groupName.toLowerCase() );
    }

    @Override
    public GroupData addGroup(String groupName)
    {
        Data group = new GroupData( plugin, groupName, "", "",
                new ArrayList<>(), new HashMap<>(), new HashMap<>() );
        groupCache.put( groupName.toLowerCase(), ( GroupData ) group );
        String json = plugin.getExclusionaryGson().toJson( group );
        putJson( groupName, json );
        try ( Jedis jedis = pool.getResource() )
        {
            jedis.sadd( PREFIX + "groups", groupName );
        }
        return ( GroupData ) group;
    }

    @Override
    public GroupData getGroup(String name)
    {
        if ( groupCache.containsKey( name.toLowerCase() ) )
        {
            return groupCache.get( name.toLowerCase() );
        }
        return updateGroup( name );
    }

    @Override
    public GroupData updateGroup(String name)
    {
        GroupData group = plugin.getExclusionaryGson().fromJson( getJson( name.toLowerCase() ), GroupData.class );
        group.setPlugin( plugin );
        group.setDataSource( this );
        groupCache.put( group.getGroupName().toLowerCase(), group );
        return group;
    }

    @Override
    public boolean playerExists(UUID playerUUID)
    {
        return playerCache.containsKey( playerUUID )
                || containsKey( PREFIX + playerUUID.toString() );
    }

    @Override
    public void setPlayerGroup(ProxiedPlayer player, String groupName)
    {
        if ( !playerExists( player.getUniqueId() ) )
        {
            PlayerData playerData = new PlayerData( this, player.getUniqueId(), groupName, "",
                    "", new HashMap<>(), new HashMap<>() );
            playerCache.put( player.getUniqueId(), playerData );
            String json = plugin.getExclusionaryGson().toJson( playerData );
            put( PREFIX + player.getUniqueId().toString(), json );
        } else
        {
            getPlayerData( player.getUniqueId() ).setGroupName( groupName );
        }
    }

    @Override
    public GroupData getPlayerGroup(UUID playerUUID)
    {
        if ( playerCache.containsKey( playerUUID ) )
            return getGroup( playerCache.get( playerUUID ).getGroupName() );
        if ( playerExists( playerUUID ) )
        {
            PlayerData playerData = plugin.getExclusionaryGson().fromJson( get( PREFIX + playerUUID ), PlayerData.class );
            playerData.setDataSource( this );
            playerCache.put( playerUUID, playerData );
            return getGroup( playerData.getGroupName() );
        }
        return null;
    }

    @Override
    public PlayerData getPlayerData(UUID playerUUID)
    {
        if ( playerCache.containsKey( playerUUID ) )
            return playerCache.get( playerUUID );
        else
        {
            PlayerData playerData = plugin.getExclusionaryGson().fromJson( get( PREFIX + playerUUID.toString() ), PlayerData.class );
            playerData.setDataSource( this );
            return playerData;
        }
    }

    public void updateGroups()
    {
        try ( Jedis jedis = pool.getResource() )
        {
            jedis.smembers( PREFIX + "groups" ).forEach( this::updateGroup );
        }
    }

    private String get(String key)
    {
        try ( Jedis jedis = pool.getResource() )
        {
            return jedis.get( key );
        }
    }

    private void put(String key, String value)
    {
        try ( Jedis jedis = pool.getResource() )
        {
            jedis.set( key, value );
        }
    }

    private boolean containsKey(String key)
    {
        try ( Jedis jedis = pool.getResource() )
        {
            return jedis.exists( key );
        }
    }

}