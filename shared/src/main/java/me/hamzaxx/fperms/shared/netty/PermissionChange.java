/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.shared.netty;

public class PermissionChange
{

    private final Change changeType;
    private final Object change;

    public PermissionChange(Change changeType, Object change) {
        this.changeType = changeType;
        this.change = change;
    }

    public Change getChangeType()
    {
        return changeType;
    }

    public Object getData()
    {
        return change;
    }
}
