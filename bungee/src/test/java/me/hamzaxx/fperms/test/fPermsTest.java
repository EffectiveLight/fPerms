/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.hamzaxx.fperms.bungee.data.GroupData;
import me.hamzaxx.fperms.bungee.util.MapMaker;
import me.hamzaxx.fperms.common.permissions.Permission;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

public class fPermsTest
{

    @Test
    public void testRegex()
    {
        String str = "stop4days";
        String[] strArry = str.split( "\\|" );
        System.out.println( strArry[ 0 ] );
    }

    @Test
    public void jsonGroupTest()
    {
        List<String> array = new ArrayList<>();
        array.add( "default" );
        array.add( "mod" );
        ConcurrentMap<String, ConcurrentMap<String, Boolean>> bungeeMap = new ConcurrentHashMap<>();
        //bungeeMap.put( "bungeecord.command.list", true );
        //bungeeMap.put( "smoke.it", false );
        //Data group = new GroupData( new RedisDataSource( fPermsPlugin.getInstance() ), "Admin", "hi", "bye", array, bungeeMap, new ConcurrentHashMap<>() );
        //String str = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson( group );
        //System.out.println( str );
    }

    @Test
    public void testArrays()
    {
        List<Integer> integers = new ArrayList<>();

        long startArray = System.nanoTime();
        for ( int l = 0; l < 100; l++ )
        {
            integers.add( l );
            if ( integers.contains( l ) ) ;
        }
        Integer[] ints1 = integers.toArray( new Integer[ integers.size() ] );
        System.out.println( System.nanoTime() - startArray );

        Set<Integer> integerSet = new HashSet<>();

        long startHash = System.nanoTime();

        for ( int i = 0; i < 100; i++ )
        {
            integerSet.add( i );
            if ( integerSet.contains( i ) ) ;
        }

        for ( int i : integerSet )
        {
            // stuff
        }
        Integer[] ints2 = integers.toArray( new Integer[ integerSet.size() ] );
        System.out.println( System.nanoTime() - startHash );
    }

    @Test
    public void testBoolean()
    {
        System.out.println( ( ( double ) 1678 / 365 ) );
        Boolean bool = Boolean.valueOf( "hi" );
        System.out.println( bool );
    }

    long expireTime = 15; // seconds
    Map<String, Map<String, Long>> map = new HashMap<>();

    @Test
    public void onChallengeCommand()
    {
        System.out.println( System.currentTimeMillis() );
        String challenger = "69swagger";
        String challenged = "austin";
        Map<String, Long> innerMap;
        if ( map.containsKey( challenged ) )
        {
            innerMap = map.get( challenged );
        } else
        {
            innerMap = new HashMap<>();
        }
        innerMap.put( challenger, System.currentTimeMillis() / 1000 );
    }

    @Test
    public void onAcceptCommand()
    {
        String challengeAcceptor = "austin";
        String challengeIssuer = "69swagger";
        if ( map.containsKey( challengeAcceptor ) )
        {
            Map<String, Long> innerMap = map.get( challengeAcceptor );
            if ( innerMap.containsKey( challengeIssuer ) )
            {
                if ( !( ( System.currentTimeMillis() / 1000 )
                        - ( innerMap.get( challengeIssuer ) + expireTime ) > 0 ) )
                {
                    // teleport
                }
                innerMap.remove( challengeIssuer );
            }
        }
    }

    @Test
    public void testComponents()
    {
        BaseComponent[] components = TextComponent.fromLegacyText( "&6penis is&d goud!" );
        for ( BaseComponent component : components )
        {
            System.out.println( component );
        }
    }

    @Test
    public void testIps()
    {
        InetSocketAddress address = new InetSocketAddress( "localhost", 25566 );
        System.out.println( address.getHostString() + " " + address.getPort() );
    }


    private final Pattern STRIP_COLORS = Pattern.compile( "(?i)&[0-9A-FK-OR]" );

    private String stripColors(String text)
    {
        return STRIP_COLORS.matcher( text ).replaceAll( "" );
    }

    @Test
    public void testChars()
    {
        System.out.println( stripColors( "&0 hi &3i &blike &1pie!" ) );
    }

    @Test
    public void testTextComponent()
    {
        TextComponent textComponent = new TextComponent( "Hello," );
        TextComponent anotherComponent = new TextComponent( "World" );
        textComponent.addExtra( " " + anotherComponent.toString() );
        System.out.println( textComponent.toLegacyText() );
    }

    @Test
    public void testMapPersistence()
    {
        ConcurrentMap<String, Boolean> map1 = new ConcurrentHashMap<>();
        map1.put( "hi1", true );
        ConcurrentMap<String, Boolean> map2 = new ConcurrentHashMap<>();
        map2.put( "hi2", true );
        MapMaker mapMaker = new MapMaker( map1 );
        MapMaker maker = new MapMaker( map2 );
        ConcurrentMap<String, MapMaker> maps = new ConcurrentHashMap<>();
        maps.put( "maker1", mapMaker );
        maps.put( "maker2", maker );
        for ( Map.Entry<String, MapMaker> mapMaker1 : maps.entrySet() )
        {
            if ( mapMaker1.getValue().equals( mapMaker ) )
            {
                maps.remove( mapMaker1.getKey() );
            }
        }

        for ( String s : maps.keySet() )
        {
            System.out.println( s );
        }
    }

    @Test
    public void testPermission()
    {
        Gson gson = new Gson();
        Map<String, Permission> permissionMap = new HashMap<>();
        Permission permission = new Permission( "bukkit.command.help", new Permission.Location( Permission.LocationType.ALL ), false );
        permissionMap.put( permission.getName(), permission );
        String json = gson.toJson( permissionMap );
        System.out.println( json );
        @SuppressWarnings("unchecked")
        Map<String, Permission> permission1 = gson.fromJson( json, HashMap.class );
        System.out.println( permission1.get( "bukkit.command.help" ).getValue() );
    }

    @Test
    public void testJson()
    {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Map<String, Permission> bukkitPerms = new HashMap<>();
        bukkitPerms.put( "smoke.it", new Permission( "smoke.it", new Permission.Location( Permission.LocationType.ALL ), false ) );
        GroupData groupData = new GroupData( null, "admin", "admin", " >", new ArrayList<>(), new HashMap<>(), bukkitPerms );
        String json = gson.toJson( groupData );
        System.out.println( json );
        GroupData data = gson.fromJson( json, GroupData.class );
        data.getBukkitPermissions().keySet().forEach( System.out::println );
    }


}
