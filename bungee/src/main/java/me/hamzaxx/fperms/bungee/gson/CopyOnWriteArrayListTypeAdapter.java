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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public final class CopyOnWriteArrayListTypeAdapter<E> extends TypeAdapter<CopyOnWriteArrayList>
{

    @SuppressWarnings("unused")
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory()
    {

        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken)
        {
            return typeToken.getRawType() == CopyOnWriteArrayList.class
                    ? ( TypeAdapter<T> ) new CopyOnWriteArrayListTypeAdapter() : null;
        }
    };

    @Override
    public synchronized CopyOnWriteArrayList read(JsonReader in) throws IOException
    {
        if ( in.peek() == JsonToken.NULL )
        {
            in.nextNull();
            return null;
        }
        Type aType = new TypeToken<ArrayList<E>>()
        {
        }.getType();
        Gson g = new Gson();
        ArrayList<E> ltm = g.fromJson( in, aType );
        return new CopyOnWriteArrayList<>( ltm );
    }

    @Override
    public synchronized void write(JsonWriter out, CopyOnWriteArrayList value) throws IOException
    {
        Gson g = new Gson();
        out.value( g.toJson( value ) );
    }
}
