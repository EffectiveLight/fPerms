/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.listeners;

import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import me.hamzaxx.fperms.bukkit.permissions.PermissionsInjector;
import me.hamzaxx.fperms.bukkit.permissions.fPermsPermissible;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinListener implements Listener
{

    private fPermsPlugin plugin;

    public JoinListener(fPermsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event)
    {
        new PermissionsInjector( event.getPlayer(),
                new fPermsPermissible( event.getPlayer(), plugin ) ).inject();
        // Permissions.addPlayer( event.getPlayer() );
    }
}
