/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.common.netty;

import com.google.gson.annotations.Expose;

public class Change
{
    @Expose
    private ChangeType changeType;
    @Expose
    private String name;
    private String data;

    public Change(ChangeType changeType, String name, String data)
    {
        this.changeType = changeType;
        this.name = name;
        this.data = data;
    }

    public Change(ChangeType changeType, String name) {
        this.changeType = changeType;
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
