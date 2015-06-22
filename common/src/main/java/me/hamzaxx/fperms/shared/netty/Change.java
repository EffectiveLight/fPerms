/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.shared.netty;

public class Change
{

    private ChangeType changeType;
    private String data;
    private String name;

    public Change(ChangeType changeType, String name, String data)
    {
        this.changeType = changeType;
        this.name = name;
        this.data = data;
    }

    public ChangeType getChangeType()
    {
        return changeType;
    }

    public String getData()
    {
        return data;
    }

    public String getName()
    {
        return name;
    }
}
