package me.hamzaxx.fperms.bukkit.permissions;

import me.hamzaxx.fperms.bukkit.data.PlayerData;
import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import me.hamzaxx.fperms.common.permissions.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
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
        PlayerData playerData = plugin.getPlayerData().get( player.getName() );
        if ( perm == null || playerData == null ) return false;
        if ( isPermissionSet( "*" ) )
        {
            Permission permission = playerData.getEffectivePermissions().get( "*" );
            switch ( permission.getLocationType() )
            {
                case ALL:
                    return permission.getValue();
                case WORLD:
                    return permission.getValue()
                            && player.getWorld().getName().equals( permission.getLocationName() );
            }
        }

        if ( isPermissionSet( perm ) )
        {
            Permission permission = playerData.getEffectivePermissions().get( perm );
            switch ( permission.getLocationType() )
            {
                case ALL:
                    return permission.getValue();
                case WORLD:
                    return permission.getValue()
                            && player.getWorld().getName().equals( permission.getLocationName() );
            }
        } else
        {
            org.bukkit.permissions.Permission bukkitPermission = Bukkit.getPluginManager().getPermission( perm );
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
    public boolean hasPermission(org.bukkit.permissions.Permission perm)
    {
        return hasPermission( perm.getName() );
    }

    @Override
    public boolean isPermissionSet(String perm)
    {
        PlayerData data = plugin.getPlayerData().get( player.getName() );
        return data != null && data.getEffectivePermissions().containsKey( perm );
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
    public boolean isPermissionSet(org.bukkit.permissions.Permission perm)
    {
        return isPermissionSet( perm.getName() );
    }
}
