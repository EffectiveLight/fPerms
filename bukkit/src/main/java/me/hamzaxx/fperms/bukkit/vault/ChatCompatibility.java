/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.vault;

import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

@SuppressWarnings("deprecation")
public class ChatCompatibility extends Chat
{

    private fPermsPlugin plugin;

    public ChatCompatibility(fPermsPlugin plugin, Permission permission)
    {
        super( permission );
        //super( Bukkit.getServicesManager().load( Permission.class ) );
        this.plugin = plugin;
    }

    @Override
    public String getName()
    {
        return plugin.getName();
    }

    @Override
    public boolean isEnabled()
    {
        return plugin.isEnabled();
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
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public void setPlayerSuffix(String world, String playerName, String suffix)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public String getGroupPrefix(String world, String groupName)
    {
        return plugin.getGroups().get( groupName ).getPrefix();
    }

    @Override
    public void setGroupPrefix(String world, String playerName, String prefix)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public String getGroupSuffix(String world, String groupName)
    {
        return plugin.getGroups().get( groupName ).getSuffix();
    }

    @Override
    public void setGroupSuffix(String world, String playerName, String suffix)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public int getPlayerInfoInteger(String s, String s1, String s2, int i)
    {
        return 0;
    }

    @Override
    public void setPlayerInfoInteger(String s, String s1, String s2, int i)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public int getGroupInfoInteger(String s, String s1, String s2, int i)
    {
        return 0;
    }

    @Override
    public void setGroupInfoInteger(String s, String s1, String s2, int i)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public double getPlayerInfoDouble(String s, String s1, String s2, double v)
    {
        return 0;
    }

    @Override
    public void setPlayerInfoDouble(String s, String s1, String s2, double v)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public double getGroupInfoDouble(String s, String s1, String s2, double v)
    {
        return 0;
    }

    @Override
    public void setGroupInfoDouble(String s, String s1, String s2, double v)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public boolean getPlayerInfoBoolean(String s, String s1, String s2, boolean b)
    {
        return false;
    }

    @Override
    public void setPlayerInfoBoolean(String s, String s1, String s2, boolean b)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public boolean getGroupInfoBoolean(String s, String s1, String s2, boolean b)
    {
        return false;
    }

    @Override
    public void setGroupInfoBoolean(String s, String s1, String s2, boolean b)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public String getPlayerInfoString(String s, String s1, String s2, String s3)
    {
        return null;
    }

    @Override
    public void setPlayerInfoString(String s, String s1, String s2, String s3)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public String getGroupInfoString(String s, String s1, String s2, String s3)
    {
        return null;
    }

    @Override
    public void setGroupInfoString(String s, String s1, String s2, String s3)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }
}
