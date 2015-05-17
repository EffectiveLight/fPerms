/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.listeners;

import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MessageListener implements Listener
{

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event)
    {
        if ( event.isCancelled() )
        {
            return;
        }

        if ( !( event.getSender() instanceof Server ) )
        {
            return;
        }

        if ( event.getTag().equals( "fPerms" ) )
        {
            event.setCancelled( true );
            try ( DataInputStream in = new DataInputStream( new ByteArrayInputStream( event.getData() ) ) )
            {
                switch ( in.readUTF() )
                {
                    // case ""
                }
            } catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

}
