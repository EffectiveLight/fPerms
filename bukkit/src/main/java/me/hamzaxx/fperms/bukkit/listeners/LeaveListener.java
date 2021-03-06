package me.hamzaxx.fperms.bukkit.listeners;

import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener
{

    private fPermsPlugin plugin;

    public LeaveListener(fPermsPlugin plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event)
    {
        handleLeave( event.getPlayer() );
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onKick(PlayerKickEvent event)
    {
        handleLeave( event.getPlayer() );
    }

    private void handleLeave(Player player)
    {
        plugin.getPlayerData().remove( player.getName() );
    }

}
