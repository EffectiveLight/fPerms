/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapMaker
{
    private ConcurrentMap<String, Boolean> map;

    public MapMaker()
    {
        map = new ConcurrentHashMap<>();
    }

    public MapMaker(ConcurrentMap<String, Boolean> map)
    {
        if ( map != null )
            this.map = map;
        else
            this.map = new ConcurrentHashMap<>();
    }

    public MapMaker put(String key, Boolean value)
    {
        map.put( key, value );
        return this;
    }

    public MapMaker remove(String key)
    {
        map.remove( key );
        return this;
    }

    public ConcurrentMap<String, Boolean> makeMap()
    {
        return map;
    }
}
