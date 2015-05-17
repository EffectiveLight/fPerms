/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data;

public enum Locations
{
    WORLD( "world:" ), SERVER( "server:" ), ALL( "all:" );

    private String prefix;

    Locations(String prefix)
    {
        this.prefix = prefix;
    }

    @Override
    public String toString()
    {
        return prefix.substring( 0, prefix.length() - 1 );
    }

    public String getPrefix()
    {
        return prefix;
    }
}
