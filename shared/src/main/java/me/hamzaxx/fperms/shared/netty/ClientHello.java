/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.shared.netty;

public class ClientHello
{

    private String serverName;

    public ClientHello(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName()
    {
        return serverName;
    }
}
