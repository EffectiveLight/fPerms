/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.listeners;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerListener implements Listener
{

    @EventHandler
    public void onServerConnect(ServerConnectEvent event)
    {
        System.out.println( "CALLED SERVER CONNECT " + event.toString() );
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent event)
    {
        TextComponent textComponent = new TextComponent( "Hello," );
        TextComponent anotherComponent = new TextComponent( "World" );
        textComponent.addExtra( " " + anotherComponent );
        event.getPlayer().sendMessage( textComponent );
    }

}
