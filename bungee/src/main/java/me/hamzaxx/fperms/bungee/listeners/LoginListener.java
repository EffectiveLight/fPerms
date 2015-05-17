/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.listeners;

import me.hamzaxx.fperms.bungee.fPermsPlugin;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener
{

    private fPermsPlugin plugin;

    public LoginListener(fPermsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event)
    {
        if ( plugin.getDataSource().getPlayerGroup( event.getPlayer().getUniqueId() ) == null )
        {
            plugin.getDataSource().setPlayerGroup( event.getPlayer(), "default" );
        }
    }
}
