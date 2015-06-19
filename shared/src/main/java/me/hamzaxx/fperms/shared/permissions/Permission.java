/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.shared.permissions;

public class Permission
{

    private String name;
    private Location location;
    private boolean value;

    public Permission(String name, Location location, boolean value)
    {
        this.name = name;
        this.location = location;
        this.value = value;
    }

    public Permission(String name, Location location) {
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
}
