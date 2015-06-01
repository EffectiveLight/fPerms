/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.listeners;

import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerListener implements Listener
{

    private long time;

    @EventHandler
    public void onServerConnect(ServerConnectEvent event)
    {
        time = System.currentTimeMillis();
        System.out.println( "CALLED SERVER CONNECT " + event.toString() );
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent event)
    {
        System.out.println( System.currentTimeMillis() - time );
        System.out.println( event.getPlayer().getLocale().getCountry() );
    }

}
