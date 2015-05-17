/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data;

import com.google.common.base.Preconditions;

public class Location
{
    private String prefix;

    public Location(Locations location, String specificLocation)
    {
        if ( location != null )
        {
            prefix = location.toString();
        } else
        {
            Preconditions.checkNotNull( specificLocation, "The specific location, can't be null." );
            //prefix
        }
    }
}
