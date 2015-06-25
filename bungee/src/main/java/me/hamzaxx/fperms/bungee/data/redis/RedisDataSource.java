/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data.redis;

import com.google.gson.Gson;
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

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class RedisDataSource implements DataSource
{

    private final fPermsPlugin plugin;
    private JedisPool pool;

    //private Gson gson;

    private final String PREFIX = "fPerms:";

    private final ConcurrentMap<String, GroupData> GROUP_CACHE = new ConcurrentHashMap<>();
    private final ConcurrentMap<UUID, PlayerData> PLAYER_GROUP_CACHE = new ConcurrentHashMap<>();

    public RedisDataSource(fPermsPlugin plugin)
    {
        this.plugin = plugin;
        /*GsonBuilder gsonBuilder = new GsonBuilder();
         gsonBuilder.registerTypeAdapter( new TypeToken<List<String>>() {}.getType(), new CopyOnWriteArrayListTypeAdapter<String>() );
        gsonBuilder.registerTypeAdapter( new TypeToken<ConcurrentMap<String, Permission>>()
        {
        }.getType(), new ConcurrentHashMapTypeAdapter<String, Permission>( plugin ) );
        gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();*/
        ProxyServer.getInstance().getScheduler().runAsync( plugin, () ->
                pool = new JedisPool( new JedisPoolConfig(), "mc.atomic-cloud.net", 6379, 0 ) );
        ProxyServer.getInstance().getScheduler().schedule( plugin, () -> {
            if ( !groupExists( "default" ) )
                addGroup( "default" );
            updateGroups();
        }, 2, TimeUnit.SECONDS );
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
        return containsKey( PREFIX + groupName.toLowerCase() );
    }

    @Override
    public GroupData addGroup(String groupName)
    {
        Data group = new GroupData( plugin, groupName, "", "",
                new ArrayList<>(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>() );
        GROUP_CACHE.put( groupName.toLowerCase(), ( GroupData ) group );
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
        if ( GROUP_CACHE.containsKey( name.toLowerCase() ) )
        {
            return GROUP_CACHE.get( name.toLowerCase() );
        }
        return updateGroup( name );
    }

    @Override
    public GroupData updateGroup(String name)
    {
        GroupData group = plugin.getGson().fromJson( getJson( name.toLowerCase() ), GroupData.class );
        group.setPlugin( plugin );
        group.setDataSource( this );
        GROUP_CACHE.put( group.getGroupName().toLowerCase(), group );
        return group;
    }

    @Override
    public boolean playerExists(UUID playerUUID)
    {
        return PLAYER_GROUP_CACHE.containsKey( playerUUID )
                || containsKey( PREFIX + playerUUID.toString() );
    }

    @Override
    public void setPlayerGroup(ProxiedPlayer player, String groupName)
    {
        if ( !playerExists( player.getUniqueId() ) )
        {
            PlayerData playerData = new PlayerData( this, player.getUniqueId(), groupName, "",
                    "", new ConcurrentHashMap<>(), new ConcurrentHashMap<>() );
            PLAYER_GROUP_CACHE.put( player.getUniqueId(), playerData );
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
        if ( PLAYER_GROUP_CACHE.containsKey( playerUUID ) )
            return getGroup( PLAYER_GROUP_CACHE.get( playerUUID ).getGroupName() );
        if ( playerExists( playerUUID ) )
        {
            PlayerData playerData = plugin.getGson().fromJson( get( PREFIX + playerUUID ), PlayerData.class );
            playerData.setDataSource( this );
            PLAYER_GROUP_CACHE.put( playerUUID, playerData );
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
            PlayerData playerData = plugin.getGson().fromJson( get( PREFIX + playerUUID.toString() ), PlayerData.class );
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