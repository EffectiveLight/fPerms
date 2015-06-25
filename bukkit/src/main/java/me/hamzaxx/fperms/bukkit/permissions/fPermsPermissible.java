/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.permissions;

import com.google.common.base.Preconditions;
import me.hamzaxx.fperms.bukkit.data.PlayerData;
import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class fPermsPermissible extends PermissibleBase
{

    private fPermsPlugin plugin;
    private Player player;

    public fPermsPermissible(Player player, fPermsPlugin plugin)
    {
        super( player );
        this.player = player;
        this.plugin = plugin;
    }

    @Override
    public boolean hasPermission(String perm)
    {
        Preconditions.checkNotNull( perm, "Permission cannot be null" );
        PlayerData playerData = plugin.getPlayerData().get( player.getName() );
        Preconditions.checkNotNull( playerData, "PlayerData cannot be null" );
        if ( isPermissionSet( "*" ) )
        {
            me.hamzaxx.fperms.common.permissions.Permission permission = playerData.getEffectivePermissions().get( "*" );
            me.hamzaxx.fperms.common.permissions.Permission.Location location = permission.getLocation();
            switch ( location.getType() )
            {
                case ALL:
                    return permission.getValue();
                case WORLD:
                    return player.getWorld().getName().equals( location.getLocationName() ) && permission.getValue();
            }
        }

        if ( isPermissionSet( perm ) )
        {
            me.hamzaxx.fperms.common.permissions.Permission permission = playerData.getEffectivePermissions().get( perm );
            me.hamzaxx.fperms.common.permissions.Permission.Location location = permission.getLocation();
            switch ( location.getType() )
            {
                case ALL:
                    return permission.getValue();
                case WORLD:
                    return player.getWorld().getName().equals( location.getLocationName() ) && permission.getValue();
            }
        } else
        {
            Permission bukkitPermission = Bukkit.getPluginManager().getPermission( perm );
            return bukkitPermission != null && bukkitPermission.getDefault().equals( PermissionDefault.TRUE );
        }
        return false;
    }

    @Override
    public void setOp(boolean value)
    {
    }

    @Override
    public boolean isOp()
    {
        return false;
    }

    @Override
    public boolean hasPermission(Permission perm)
    {
        return hasPermission( perm.getName() );
    }

    @Override
    public boolean isPermissionSet(String perm)
    {
        PlayerData data = plugin.getPlayerData().get( player.getName() );
        Preconditions.checkNotNull( data );
        return data.getEffectivePermissions().containsKey( perm );
    }

    @Override
    public void recalculatePermissions()
    {
    }

    @Override
    public synchronized void clearPermissions()
    {
    }

    @Override
    public boolean isPermissionSet(Permission perm)
    {
        return isPermissionSet( perm.getName() );
    }
}
