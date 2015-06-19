/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.vault;

import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;

@SuppressWarnings("deprecation")
public class ChatCompatibility extends Chat
{

    private fPermsPlugin plugin;

    public ChatCompatibility(fPermsPlugin plugin)
    {
        super( Bukkit.getServicesManager().load( Permission.class ) );
        this.plugin = plugin;
    }

    @Override
    public String getName()
    {
        return "fPerms";
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public String getPlayerPrefix(String world, String playerName)
    {
        return plugin.getPlayerData().get( playerName ).getPrefix();
    }

    @Override
    public String getPlayerSuffix(String world, String playerName)
    {
        return plugin.getPlayerData().get( playerName ).getSuffix();
    }

    @Override
    public void setPlayerPrefix(String world, String playerName, String prefix)
    {
        plugin.getPlayerData().get( playerName ).setPrefix( prefix );
    }

    @Override
    public void setPlayerSuffix(String world, String playerName, String suffix)
    {
        plugin.getPlayerData().get( playerName ).setSuffix( suffix );
    }

    @Override
    public String getGroupPrefix(String world, String playerName)
    {
       return plugin.getPlayerData().get( playerName ).getGroup().getPrefix();
    }

    @Override
    public void setGroupPrefix(String world, String playerName, String prefix)
    {
        plugin.getPlayerData().get( playerName ).getGroup().setPrefix( prefix );
    }

    @Override
    public String getGroupSuffix(String world, String playerName)
    {
        return plugin.getPlayerData().get( playerName ).getGroup().getSuffix();
    }

    @Override
    public void setGroupSuffix(String world, String playerName, String suffix)
    {
        plugin.getPlayerData().get( playerName ).getGroup().setSuffix( suffix );
    }

    @Override
    public int getPlayerInfoInteger(String s, String s1, String s2, int i)
    {
        return 0;
    }

    @Override
    public void setPlayerInfoInteger(String s, String s1, String s2, int i)
    {

    }

    @Override
    public int getGroupInfoInteger(String s, String s1, String s2, int i)
    {
        return 0;
    }

    @Override
    public void setGroupInfoInteger(String s, String s1, String s2, int i)
    {

    }

    @Override
    public double getPlayerInfoDouble(String s, String s1, String s2, double v)
    {
        return 0;
    }

    @Override
    public void setPlayerInfoDouble(String s, String s1, String s2, double v)
    {

    }

    @Override
    public double getGroupInfoDouble(String s, String s1, String s2, double v)
    {
        return 0;
    }

    @Override
    public void setGroupInfoDouble(String s, String s1, String s2, double v)
    {

    }

    @Override
    public boolean getPlayerInfoBoolean(String s, String s1, String s2, boolean b)
    {
        return false;
    }

    @Override
    public void setPlayerInfoBoolean(String s, String s1, String s2, boolean b)
    {

    }

    @Override
    public boolean getGroupInfoBoolean(String s, String s1, String s2, boolean b)
    {
        return false;
    }

    @Override
    public void setGroupInfoBoolean(String s, String s1, String s2, boolean b)
    {

    }

    @Override
    public String getPlayerInfoString(String s, String s1, String s2, String s3)
    {
        return null;
    }

    @Override
    public void setPlayerInfoString(String s, String s1, String s2, String s3)
    {

    }

    @Override
    public String getGroupInfoString(String s, String s1, String s2, String s3)
    {
        return null;
    }

    @Override
    public void setGroupInfoString(String s, String s1, String s2, String s3)
    {

    }
}
