/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.vault;

import com.google.common.base.Preconditions;
import me.hamzaxx.fperms.bukkit.data.PlayerData;
import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import net.milkbowl.vault.permission.Permission;

@SuppressWarnings("deprecation")
public class PermissionsCompatibility extends Permission
{

    private fPermsPlugin plugin;

    public PermissionsCompatibility(fPermsPlugin plugin)
    {
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
    public boolean hasSuperPermsCompat()
    {
        return true;
    }

    @Override
    public boolean playerHas(String world, String player, String permission)
    {
        PlayerData data = plugin.getPlayerData().get( player );
        Preconditions.checkNotNull( data );
        return data.getEffectivePermissions().containsKey( permission );
    }

    @Override
    public boolean playerAdd(String s, String s1, String s2)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public boolean playerRemove(String s, String s1, String s2)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public boolean groupHas(String s, String s1, String s2)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public boolean groupAdd(String s, String s1, String s2)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public boolean groupRemove(String s, String s1, String s2)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public boolean playerInGroup(String world, String player, String group)
    {
        PlayerData data = plugin.getPlayerData().get( player );
        Preconditions.checkNotNull( data );
        return data.getName().equals( group );
    }

    @Override
    public boolean playerAddGroup(String s, String s1, String s2)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public boolean playerRemoveGroup(String s, String s1, String s2)
    {
        throw new UnsupportedOperationException( "fPerms API is read only!" );
    }

    @Override
    public String[] getPlayerGroups(String playerName, String groupName)
    {

        return new String[]{ plugin.getPlayerData().get( playerName ).getName() };
    }

    @Override
    public String getPrimaryGroup(String world, String player)
    {
        PlayerData data = plugin.getPlayerData().get( player );
        Preconditions.checkNotNull( data );
        return data.getName();
    }

    @Override
    public String[] getGroups()
    {
        return plugin.getGroups().keySet().toArray( new String[ plugin.getGroups().size() ] );
    }

    @Override
    public boolean hasGroupSupport()
    {
        return true;
    }
}
