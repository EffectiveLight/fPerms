/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.vault;

import me.hamzaxx.fperms.bukkit.Permissions;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

@SuppressWarnings("deprecation")
public class ChatCompatibility extends Chat
{

    private static ChatCompatibility instance;

    public ChatCompatibility(Permission perms)
    {
        super( perms );
        instance = this;
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

    public static ChatCompatibility getInstance()
    {
        return instance;
    }

    @Override
    public String getPlayerPrefix(String world, String playerName)
    {
        return Permissions.getPlayerData().get( playerName ).getPlayerPrefix();
    }

    @Override
    public String getPlayerSuffix(String world, String playerName)
    {
        return Permissions.getPlayerData().get( playerName ).getPlayerSuffix();
    }

    @Override
    public void setPlayerPrefix(String s, String s1, String s2)
    {
    }

    @Override
    public void setPlayerSuffix(String s, String s1, String s2)
    {
    }

    @Override
    public String getGroupPrefix(String world, String playerName)
    {
        return Permissions.getPlayerData().get( playerName ).getGroupPrefix();
    }

    @Override
    public void setGroupPrefix(String s, String s1, String s2)
    {

    }

    @Override
    public String getGroupSuffix(String world, String playerName)
    {
        return Permissions.getPlayerData().get( playerName ).getGroupSuffix();
    }

    @Override
    public void setGroupSuffix(String s, String s1, String s2)
    {

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
