/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.test;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.hamzaxx.fperms.bungee.data.Data;
import me.hamzaxx.fperms.bungee.data.GroupData;
import me.hamzaxx.fperms.bungee.data.redis.RedisDataSource;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import me.hamzaxx.fperms.bungee.gson.ConcurrentHashMapTypeAdapter;
import me.hamzaxx.fperms.bungee.gson.CopyOnWriteArrayListTypeAdapter;
import me.hamzaxx.fperms.bungee.util.MapMaker;
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
        Data group = new GroupData( new RedisDataSource( fPermsPlugin.getInstance() ), "Admin", "hi", "bye", array,
                bungeeMap, new ConcurrentHashMap<>() );
        String str = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson( group );
        System.out.println( str );
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

    @org.junit.Test
    public void testStuff()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter( new TypeToken<List<String>>()
        {
        }.getType(), new CopyOnWriteArrayListTypeAdapter<String>() );
        gsonBuilder.registerTypeAdapter( new TypeToken<ConcurrentMap<String, Boolean>>()
        {
        }.getType(), new ConcurrentHashMapTypeAdapter<String, Boolean>( fPermsPlugin.getInstance() ) );
        String string = "{\"name\":\"Admin\",\"prefix\":\"hi\",\"suffix\":\"bye\",\"parents\":[\"default\",\"mod\"]," +
                "\"permissions\":{\"smoke.it\":false,\"i.lek.pie\":true}}";
        Gson gsonWithTypeAdapter = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
        Data group = gsonWithTypeAdapter.fromJson( string, GroupData.class );
        System.out.println( group.getGroupName() );
        System.out.println( group.getPrefix() );
        System.out.println( group.getSuffix() );
        group.getParents().forEach( System.out::println );
        String json = gsonWithTypeAdapter.toJson( group );
        System.out.println( json );
        // System.out.println( group.getEffectiveBungeePermissions() );
        // System.out.println( group.getEffectivePermissionsJson() );
    }

    @Test
    public void testMaps()
    {
        Map<String, Boolean> map = new ConcurrentHashMap<>();
        map.put( "plotme.admin", true );
        map.put( "is.gay", false );
        map.put( "is.caleb.watching", true );
        String json = fPermsPlugin.getInstance().getGson().toJson( map );
        System.out.println( json );
        @SuppressWarnings("unchecked")
        Map<String, Boolean> map1 = fPermsPlugin.getInstance().getGson().fromJson( json, HashMap.class );
        for ( Map.Entry<String, Boolean> entry : map1.entrySet() )
        {
            System.out.println( entry.getKey() + " " + entry.getValue() );
        }
    }

    @Test
    public void testBoolean()
    {
        System.out.println( ( ( double ) 1678 / 365 ) );
        Boolean bool = Boolean.valueOf( "hi" );
        System.out.println( bool );
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
    public void testMath()
    {
        int vanityCredits = 100;
        System.out.println( vanityCredits * 100 );

        int hypixelCredits = 0;
        for ( int i = 0; i < vanityCredits; i++ )
        {
            hypixelCredits += i * 100;
        }
        System.out.println( hypixelCredits );
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

}
