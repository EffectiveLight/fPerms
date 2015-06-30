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
    private boolean value;
    @Expose
    private LocationType locationType;
    @Expose
    private String locationName;

    public Permission(String name, boolean value, LocationType locationType, String locationName)
    {
        this.name = name;
        this.value = value;
        this.locationType = locationType;
        this.locationName = locationName;
    }

    public Permission(String name, boolean value, LocationType locationType)
    {
        this.name = name;
        this.value = value;
        this.locationType = locationType;
    }

    public String getName()
    {
        return name;
    }

    public LocationType getLocationType()
    {
        return locationType;
    }

    public String getLocationName()
    {
        return locationName;
    }

    public boolean getValue()
    {
        return value;
    }

    public enum LocationType
    {
        WORLD, SERVER, BUNGEE, BUKKIT, ALL
    }
}
