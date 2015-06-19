/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data;

import com.google.gson.annotations.Expose;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import me.hamzaxx.fperms.shared.permissions.Permission;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PlayerData implements Data
{

    private UUID playerUUID;
    @Expose
    private String group;
    @Expose
    private String prefix;
    @Expose
    private String suffix;
    //private String effectiveBukkitPermissionsJson;

    @Expose
    private ConcurrentMap<String, Permission> bukkitPermissions;
    @Expose
    private ConcurrentMap<String, Permission> bungeePermissions;

    private ConcurrentMap<String, Permission> effectiveBukkitPermissions;

    private fPermsPlugin plugin;
    private DataSource dataSource;

    public PlayerData(fPermsPlugin plugin, DataSource dataSource, UUID playerUUID,
                      String group, String prefix, String suffix, ConcurrentMap<String, Permission>
                              bukkitPermissions, ConcurrentMap<String, Permission> bungeePermissions)
    {
        this.plugin = plugin;
        this.dataSource = dataSource;
        this.playerUUID = playerUUID;
        this.group = group;
        this.prefix = prefix;
        this.suffix = suffix;
        this.bukkitPermissions = bukkitPermissions;
        this.bungeePermissions = bungeePermissions;
    }

    @Override
    public String getGroupName()
    {
        return group;
    }

    public void setGroupName(String groupName)
    {
        this.group = groupName;
        dataSource.savePlayer( this );
    }

    @Override
    public String getPrefix()
    {
        return prefix;
    }

    @Override
    public String getSuffix()
    {
        return suffix;
    }

    @Override
    public ConcurrentMap<String, Permission> getBukkitPermissions()
    {
        return bukkitPermissions;
    }

    @Override
    public ConcurrentMap<String, Permission> getBungeePermissions()
    {
        return bungeePermissions;
    }

    @Override
    public ConcurrentMap<String, Permission> getEffectiveBungeePermissions()
    {
        if ( bukkitPermissions != null )
        {
            return bukkitPermissions;
        } else
        {
            return computeEffectiveBungeePermissions();
        }
    }

    @Override
    public ConcurrentMap<String, Permission> getEffectiveBukkitPermissions()
    {
        if ( effectiveBukkitPermissions != null )
        {
            return effectiveBukkitPermissions;
        } else
        {
            return computeEffectiveBukkitPermissions();
        }
    }

    /*@Override
    public String getEffectiveBukkitPermissionsJson()
    {
        if ( effectiveBukkitPermissionsJson != null )
        {
            return effectiveBukkitPermissionsJson;
        } else
        {
            return computeEffectiveBukkitPermissionsJson();
        }
    }*/

    @Override
    public List<String> getParents()
    {
        throw new UnsupportedOperationException( "Player's don't have parents!" );
    }

    @Override
    public void setBungeePermission(Permission permission)
    {
        getBungeePermissions().put( permission.getName(), permission );
        computeEffectiveBungeePermissions();
        dataSource.savePlayer( this );
    }

    @Override
    public void setBukkitPermission(Permission permission)
    {
        getBukkitPermissions().put( permission.getName(), permission );
        computeEffectiveBukkitPermissions();
    }

    @Override
    public boolean unsetBungeePermission(Permission permission)
    {
        if ( !getBungeePermissions().containsKey( permission.getName() ) ) return false;
        getBukkitPermissions().remove( permission.getName() );
        computeEffectiveBungeePermissions();
        dataSource.savePlayer( this );
        return true;
    }

    @Override
    public boolean unsetBukkitPermission(Permission permission)
    {
        if ( !getBukkitPermissions().containsKey( permission.getName() ) ) return false;
        getBukkitPermissions().remove( permission.getName() );
        computeEffectiveBukkitPermissions();
        dataSource.savePlayer( this );
        return true;
    }

    @Override
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
        dataSource.savePlayer( this );
    }

    @Override
    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
        dataSource.savePlayer( this );
    }

    @Override
    @Deprecated
    public void addParent(String parent)
    {
        throw new UnsupportedOperationException( "Not applicable!" );
    }

    @Override
    @Deprecated
    public void addParents(List<String> parents)
    {
        throw new UnsupportedOperationException( "Not applicable!" );
    }

    public ConcurrentMap<String, Permission> computeEffectiveBukkitPermissions()
    {
        ConcurrentMap<String, Permission> tempMap = new ConcurrentHashMap<>();
        tempMap.putAll( dataSource.getPlayerGroup( playerUUID ).getEffectiveBukkitPermissions() );
        tempMap.putAll( getBukkitPermissions() );
        this.effectiveBukkitPermissions = tempMap;
        return tempMap;
    }

    public ConcurrentMap<String, Permission> computeEffectiveBungeePermissions()
    {
        ConcurrentMap<String, Permission> tempMap = new ConcurrentHashMap<>();
        tempMap.putAll( dataSource.getPlayerGroup( playerUUID ).getEffectiveBungeePermissions() );
        tempMap.putAll( getBungeePermissions() );
        this.effectiveBukkitPermissions = tempMap;
        return tempMap;
    }

    /*public String computeEffectiveBukkitPermissionsJson()
    {
        String json = plugin.getGson().toJson( getEffectiveBukkitPermissions() );
        this.effectiveBukkitPermissionsJson = json;
        return json;
    }*/

    public UUID getPlayerUUID()
    {
        return playerUUID;
    }
}
