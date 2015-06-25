/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.common.permissions;

import com.google.gson.annotations.Expose;

public class Permission
{

    @Expose
    private String name;
    @Expose
    private Location location;
    @Expose
    private boolean value;

    public Permission(String name, Location location, boolean value)
    {
        this.name = name;
        this.location = location;
        this.value = value;

    }

    public Permission(String name, Location location)
    {
        this.name = name;
        this.location = location;
    }

    public String getName()
    {
        return name;
    }

    public Location getLocation()
    {
        return location;
    }

    public boolean getValue()
    {
        return value;
    }

    public static class Location
    {

        @Expose
        private LocationType locationType;
        @Expose
        private String locationName;

        public Location(LocationType type, String locationName)
        {
            this.locationType = type;
            this.locationName = locationName;
        }

        public Location(LocationType locationType)
        {
            this.locationType = locationType;
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


    public enum LocationType
    {
        WORLD, SERVER, ALL
    }
}
