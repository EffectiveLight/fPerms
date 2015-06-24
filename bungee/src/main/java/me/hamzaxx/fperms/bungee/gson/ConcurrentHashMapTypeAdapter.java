/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import me.hamzaxx.fperms.bungee.fPermsPlugin;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class ConcurrentHashMapTypeAdapter<K, V> extends TypeAdapter<ConcurrentMap<K, V>>
{

    private static fPermsPlugin plugin;

    public ConcurrentHashMapTypeAdapter(fPermsPlugin plugin)
    {
        ConcurrentHashMapTypeAdapter.plugin = plugin;
    }

    @SuppressWarnings("unused")
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory()
    {

        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken)
        {
            return typeToken.getRawType() == ConcurrentMap.class
                    ? ( TypeAdapter<T> ) new ConcurrentHashMapTypeAdapter( plugin ) : null;
        }
    };

    @Override
    public synchronized ConcurrentMap<K, V> read(JsonReader in) throws IOException
    {
        if ( in.peek() == JsonToken.NULL )
        {
            in.nextNull();
            return null;
        }
        Type aType = new TypeToken<Map<K, V>>() {}.getType();
        Gson g = plugin.getExclusionaryGson();
        Map<K, V> ltm = g.fromJson( in, aType );
        return new ConcurrentHashMap<>( ltm );
    }

    @Override
    public synchronized void write(JsonWriter out, ConcurrentMap<K, V> value) throws IOException
    {
        Gson g = plugin.getExclusionaryGson();
        out.value( g.toJson( value ) );
    }
}
