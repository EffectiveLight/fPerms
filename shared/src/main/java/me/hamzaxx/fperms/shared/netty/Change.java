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

    public Change(ChangeType changeType, String data, String name)
    {
        this.changeType = changeType;
        this.data = data;
        this.name = name;
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
