/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.shared.netty;

public class ServerBye
{
    private String reason;

    public ServerBye(String reason)
    {

        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }
}