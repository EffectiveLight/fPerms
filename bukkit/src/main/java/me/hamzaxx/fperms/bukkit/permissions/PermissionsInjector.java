/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.permissions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class PermissionsInjector
{
    private static String version;
    private static Field permField;
    private Player player;
    private fPermsPermissible permissible;

    public PermissionsInjector(@Nonnull Player player, @Nullable fPermsPermissible permissible) {
        this.player = player;
        this.permissible = permissible;
    }

    static
    {
        version = Bukkit.getServer().getClass().getPackage().getName().split( "\\." )[ 3 ];
        try
        {
            final Class<?> craftHumanEntityClass = Class.forName( getCraftHumanEntityClassPath() );
            permField = craftHumanEntityClass.getDeclaredField( "perm" );
            permField.setAccessible( true );
        } catch ( ClassNotFoundException | NoSuchFieldException e )
        {
            e.printStackTrace();
        }
    }

    public void inject() {
        try
        {
            permField.set( player, permissible );
        } catch ( IllegalAccessException e )
        {
            e.printStackTrace();
        }
    }

    private static String getCraftHumanEntityClassPath() {
        return "org.bukkit.craftbukkit." + version + ".entity.CraftHumanEntity";
    }
}
