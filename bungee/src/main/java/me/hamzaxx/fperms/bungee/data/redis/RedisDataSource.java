/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.hamzaxx.fperms.bungee.data.Data;
import me.hamzaxx.fperms.bungee.data.DataSource;
import me.hamzaxx.fperms.bungee.data.GroupData;
import me.hamzaxx.fperms.bungee.data.PlayerData;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import me.hamzaxx.fperms.bungee.gson.ConcurrentHashMapTypeAdapter;
import me.hamzaxx.fperms.bungee.gson.CopyOnWriteArrayListTypeAdapter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class RedisDataSource implements DataSource
{

    private final fPermsPlugin plugin;
    private JedisPool pool;

    private Gson gson;

    private final String PREFIX = "fPerms:";

    private final ConcurrentMap<String, GroupData> GROUP_CACHE = new ConcurrentHashMap<>();
    private final ConcurrentMap<UUID, PlayerData> PLAYER_GROUP_CACHE = new ConcurrentHashMap<>();

    public RedisDataSource(fPermsPlugin plugin)
    {
        this.plugin = plugin;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter( new TypeToken<List<String>>()
        {
        }.getType(), new CopyOnWriteArrayListTypeAdapter<String>() );
        gsonBuilder.registerTypeAdapter( new TypeToken<ConcurrentMap<String, Boolean>>()
        {
        }.getType(), new ConcurrentHashMapTypeAdapter<String, Boolean>( plugin ) );
        gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
        ProxyServer.getInstance().getScheduler().runAsync( plugin, () ->
                pool = new JedisPool( new JedisPoolConfig(), "104.154.40.201", 6379, 0 ) );
        ProxyServer.getInstance().getScheduler().schedule( plugin, () -> {
            if ( !groupExists( "Default" ) )
                addGroup( "Default" );
            updateGroups();
        }, 3, TimeUnit.SECONDS );
    }

    @Override
    public ConcurrentMap<UUID, PlayerData> getPlayerCache()
    {
        return PLAYER_GROUP_CACHE;
    }

    @Override
    public ConcurrentMap<String, GroupData> getGroups()
    {
        return GROUP_CACHE;
    }

    private String getJson(String groupName)
    {
        return get( PREFIX + groupName );
    }

    private void putJson(String groupName, String json)
    {
        put( PREFIX + groupName.toLowerCase(), json );
    }

    @Override
    public void saveGroup(GroupData groupData)
    {
        String json = plugin.getGson().toJson( groupData );
        putJson( groupData.getGroupName(), json );
    }

    @Override
    public void savePlayer(PlayerData playerData)
    {
        String json = plugin.getGson().toJson( playerData );
        put( PREFIX + playerData.getPlayerUUID().toString(), json );
    }

    @Override
    public boolean groupExists(String groupName)
    {
        return containsKey( PREFIX + groupName.toLowerCase() );
    }

    @Override
    public GroupData addGroup(String groupName)
    {
        Data group = new GroupData( this, groupName, "", "",
                new CopyOnWriteArrayList<>(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>() );
        GROUP_CACHE.put( groupName.toLowerCase(), ( GroupData ) group );
        String json = plugin.getGson().toJson( group );
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
        if ( GROUP_CACHE.containsKey( name.toLowerCase() ) )
        {
            if ( containsKey( PREFIX + name.toLowerCase() ) )
            {
                return GROUP_CACHE.get( name.toLowerCase() );
            }
            return null;
        }
        return updateGroup( name );
    }

    @Override
    public GroupData updateGroup(String name)
    {
        Data group = gson.fromJson( getJson( name.toLowerCase() ), GroupData.class );
        GROUP_CACHE.put( group.getGroupName().toLowerCase(), ( GroupData ) group );
        return ( GroupData ) group;
    }

    @Override
    public boolean playerExists(UUID playerUUID)
    {
        return containsKey( PREFIX + playerUUID.toString() );
    }

    @Override
    public void setPlayerGroup(ProxiedPlayer player, String groupName)
    {
        Data playerData = new PlayerData( plugin, this, player.getUniqueId(), groupName, "",
                "", new ConcurrentHashMap<>(), new ConcurrentHashMap<>() );
        PLAYER_GROUP_CACHE.put( player.getUniqueId(), ( PlayerData ) playerData );
        String json = plugin.getGson().toJson( playerData );
        put( PREFIX + player.getUniqueId().toString(), json );
    }

    @Override
    public GroupData getPlayerGroup(UUID playerUUID)
    {
        if ( PLAYER_GROUP_CACHE.containsKey( playerUUID ) )
            return getGroup( PLAYER_GROUP_CACHE.get( playerUUID ).getGroupName() );
        if ( playerExists( playerUUID ) )
        {
            Data playerData = gson.fromJson( get( PREFIX + playerUUID ), PlayerData.class );
            PLAYER_GROUP_CACHE.put( playerUUID, ( PlayerData ) playerData );
            return getGroup( playerData.getGroupName() );
        }
        return null;
    }

    @Override
    public PlayerData getPlayerData(UUID playerUUID)
    {
        if ( PLAYER_GROUP_CACHE.containsKey( playerUUID ) )
            return PLAYER_GROUP_CACHE.get( playerUUID );
        else
        {
            Data playerData = gson.fromJson( get( PREFIX + playerUUID.toString() ), PlayerData.class );
            return ( PlayerData ) playerData;
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