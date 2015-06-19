/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.shared.permissions;

public class Location
{

    private final LocationType locationType;
    private final String locationName;

    public Location(LocationType type, String locationName) {
        this.locationType = type;
        this.locationName = locationName;
    }

    public LocationType getType()
    {
        return locationType;
    }

    public String getLocationName()
    {
        return locationName;
    }
}