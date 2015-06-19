/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.listeners;

import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class CommandListener implements Listener
{
    private fPermsPlugin plugin;

    public CommandListener(fPermsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event)
    {
        if ( event.getCommand().toLowerCase().startsWith( "reload" ) )
        {
            try ( ObjectOutputStream outputStream = new ObjectOutputStream(
                    new FileOutputStream( plugin.getDataFolder().getPath() + File.separator + "temp.dat" ) ) )
            {
                outputStream.writeObject( plugin.getPlayerData() );
                outputStream.writeObject( plugin.getGroups() );
            } catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event)
    {
        if ( event.getMessage().toLowerCase().startsWith( "/reload" ) )
        {
            try ( ObjectOutputStream outputStream = new ObjectOutputStream(
                    new FileOutputStream( plugin.getDataFolder().getPath() + File.separator + "temp.dat" ) ) )
            {
                outputStream.writeObject( plugin.getPlayerData() );
                outputStream.writeObject( plugin.getGroups() );
            } catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

}
