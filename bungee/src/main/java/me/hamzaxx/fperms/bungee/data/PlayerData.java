/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data;

import com.google.gson.annotations.Expose;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import me.hamzaxx.fperms.bungee.util.MapMaker;

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
    private String effectiveBukkitPermissionsJson;

    @Expose
    private ConcurrentMap<String, ConcurrentMap<String, Boolean>> bukkitPermissions;
    @Expose
    private ConcurrentMap<String, ConcurrentMap<String, Boolean>> bungeePermissions;

    private ConcurrentMap<String, ConcurrentMap<String, Boolean>> effectiveBukkitPermissions;

    private fPermsPlugin plugin;
    private DataSource dataSource;

    public PlayerData(fPermsPlugin plugin, DataSource dataSource, UUID playerUUID,
                      String group, String prefix, String suffix, ConcurrentMap<String, ConcurrentMap<String, Boolean>>
                              bukkitPermissions, ConcurrentMap<String, ConcurrentMap<String, Boolean>> bungeePermissions)
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
    public ConcurrentMap<String, ConcurrentMap<String, Boolean>> getBukkitPermissions()
    {
        return bukkitPermissions;
    }

    @Override
    public ConcurrentMap<String, ConcurrentMap<String, Boolean>> getBungeePermissions()
    {
        return bungeePermissions;
    }

    @Override
    public ConcurrentMap<String, ConcurrentMap<String, Boolean>> getEffectiveBungeePermissions()
    {
        if ( bukkitPermissions != null )
        {
            return bukkitPermissions;
        } else
        {
            return computeEffectivePlayerBukkitPermissions();
        }
    }

    @Override
    public ConcurrentMap<String, ConcurrentMap<String, Boolean>> getEffectiveBukkitPermissions()
    {
        if ( effectiveBukkitPermissions != null )
        {
            return effectiveBukkitPermissions;
        } else
        {
            return computeEffectivePlayerBukkitPermissions();
        }
    }

    @Override
    public String getEffectiveBukkitPermissionsJson()
    {
        if ( effectiveBukkitPermissionsJson != null )
        {
            return effectiveBukkitPermissionsJson;
        } else
        {
            return computeEffectiveBukkitPermissionsJson();
        }
    }

    @Override
    public List<String> getParents()
    {
        throw new UnsupportedOperationException( "Player's don't have parents!" );
    }

    @Override
    public void setBungeePermission(String location, String permission, boolean value)
    {
        getBungeePermissions().put( location, new MapMaker(
                getBukkitPermissions().get( location ) ).put( permission, value ).makeMap() );
        computeEffectivePlayerBungeePermissions();
        dataSource.savePlayer( this );
    }

    @Override
    public void setBukkitPermission(String location, String permission, boolean value)
    {
        getBukkitPermissions().put( permission, new MapMaker(
                getBukkitPermissions().get( location ) ).put( permission, value ).makeMap() );
        computeEffectivePlayerBukkitPermissions();
    }

    @Override
    public boolean unsetBungeePermission(String location, String permission)
    {
        if ( !getBungeePermissions().containsKey( permission ) ) return false;
        getBukkitPermissions().remove( permission );
        computeEffectivePlayerBungeePermissions();
        dataSource.savePlayer( this );
        return true;
    }

    @Override
    public boolean unsetBukkitPermission(String location, String permission)
    {
        if ( !getBukkitPermissions().containsKey( permission ) ) return false;
        getBukkitPermissions().remove( permission );
        computeEffectivePlayerBukkitPermissions();
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

    public ConcurrentMap<String, ConcurrentMap<String, Boolean>> computeEffectivePlayerBukkitPermissions()
    {
        ConcurrentMap<String, ConcurrentMap<String, Boolean>> tempMap = new ConcurrentHashMap<>();
        tempMap.putAll( dataSource.getPlayerGroup( playerUUID ).getEffectiveBukkitPermissions() );
        tempMap.putAll( getBukkitPermissions() );
        this.effectiveBukkitPermissions = tempMap;
        return tempMap;
    }

    public ConcurrentMap<String, ConcurrentMap<String, Boolean>> computeEffectivePlayerBungeePermissions()
    {
        ConcurrentMap<String, ConcurrentMap<String, Boolean>> tempMap = new ConcurrentHashMap<>();
        tempMap.putAll( dataSource.getPlayerGroup( playerUUID ).getEffectiveBungeePermissions() );
        tempMap.putAll( getBungeePermissions() );
        this.effectiveBukkitPermissions = tempMap;
        return tempMap;
    }

    public String computeEffectiveBukkitPermissionsJson()
    {
        String json = plugin.getGson().toJson( getEffectiveBukkitPermissions() );
        this.effectiveBukkitPermissionsJson = json;
        return json;
    }

    public UUID getPlayerUUID()
    {
        return playerUUID;
    }
}
