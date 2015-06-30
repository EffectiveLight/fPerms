/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data;

import com.google.gson.annotations.Expose;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import me.hamzaxx.fperms.common.netty.Change;
import me.hamzaxx.fperms.common.netty.ChangeType;
import me.hamzaxx.fperms.common.permissions.Permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupData implements Data
{

    @Expose
    private String name;
    @Expose
    private String prefix;
    @Expose
    private String suffix;

    private fPermsPlugin plugin;
    private DataSource dataSource;

    @Expose
    private List<String> parents;
    @Expose
    private Map<String, Permission> bungeePermissions;
    @Expose
    private Map<String, Permission> bukkitPermissions;

    private Map<String, Permission> effectiveBungeePermissions;
    private Map<String, Permission> effectiveBukkitPermissions;

    public GroupData(fPermsPlugin plugin, String name, String prefix, String suffix, List<String> parents,
                     Map<String, Permission> bungeePermissions, Map<String, Permission> bukkitPermissions)
    {
        this.plugin = plugin;
        this.dataSource = plugin.getDataSource();
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.parents = parents;
        this.bungeePermissions = bungeePermissions;
        this.bukkitPermissions = bukkitPermissions;
    }

    public void setPlugin(fPermsPlugin plugin)
    {
        this.plugin = plugin;
    }

    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public String getGroupName()
    {
        return name;
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
    public Map<String, Permission> getBukkitPermissions()
    {
        return bukkitPermissions;
    }

    public List<String> getParents()
    {
        return parents;
    }

    @Override
    public Map<String, Permission> getBungeePermissions()
    {
        return bungeePermissions;
    }


    @Override
    public Map<String, Permission> getEffectiveBungeePermissions()
    {
        if ( effectiveBungeePermissions != null )
        {
            return effectiveBungeePermissions;
        } else
        {
            return computeEffectiveBungeePermissions();
        }
    }

    @Override
    public Map<String, Permission> getEffectiveBukkitPermissions()
    {
        if ( effectiveBukkitPermissions != null )
        {
            return effectiveBukkitPermissions;
        } else
        {
            return computeEffectiveBukkitPermissions();
        }
    }

    @Override
    public boolean unsetBungeePermission(String permission)
    {
        if ( getBungeePermissions().containsKey( permission ) )
        {
            getBungeePermissions().remove( permission );
            computeEffectiveBungeePermissions();
            if ( getBungeePermissions().get( permission ).getValue() )
            {
                dataSource.getGroups().values().stream().filter( parent ->
                        parent.getParents().contains( getGroupName() ) )
                        .forEach( GroupData::computeEffectiveBungeePermissions );
            }
            dataSource.getPlayerCache().values().stream().filter( playerData ->
                    playerData.getGroupName().equalsIgnoreCase( getGroupName() ) ).forEach(
                    PlayerData::computeEffectiveBungeePermissions );
            dataSource.saveGroup( this );
            return true;
        }
        return false;
    }

    @Override
    public boolean unsetBukkitPermission(String permission)
    {
        if ( getBukkitPermissions().containsKey( permission ) )
        {
            getBukkitPermissions().remove( permission );
            computeEffectiveBukkitPermissions();
            if ( getBukkitPermissions().get( permission ).getValue() )
            {
                dataSource.getGroups().values().stream().filter( parent ->
                        parent.getParents().contains( getGroupName() ) ).forEach( parent ->
                        plugin.sentToAll( new Change( ChangeType.REFRESH_PERMISSIONS,
                                parent.getGroupName(), plugin.getGson().toJson(
                                parent.computeEffectiveBukkitPermissions() ) ) ) );
            }
            dataSource.saveGroup( this );
            return true;
        }
        return false;
    }

    @Override
    public void setBungeePermission(Permission permission)
    {
        getBungeePermissions().put( permission.getName(), permission );
        getEffectiveBungeePermissions().put( permission.getName(), permission );
        dataSource.saveGroup( this );
        if ( permission.getValue() )
        {
            dataSource.getGroups().values().stream().filter( group ->
                    group.getParents().contains( getGroupName() ) ).forEach( group ->
                    group.getEffectiveBungeePermissions().put( permission.getName(), permission ) );
        }
        dataSource.getPlayerCache().values().stream().filter( playerData ->
                playerData.getGroupName().equalsIgnoreCase( getGroupName() ) ).forEach(
                PlayerData::computeEffectiveBungeePermissions );
    }

    @Override
    public void setBukkitPermission(Permission permission)
    {
        getBukkitPermissions().put( permission.getName(), permission );
        getEffectiveBukkitPermissions().put( permission.getName(), permission );
        dataSource.saveGroup( this );
        if ( permission.getValue() )
        {
            dataSource.getGroups().values().stream().filter( group ->
                    group.getParents().contains( getGroupName() ) ).forEach( group -> {
                group.getEffectiveBungeePermissions().put( permission.getName(), permission );
                plugin.sentToAll( new Change( ChangeType.SET_GROUP_PERMISSION, group.getGroupName(),
                        plugin.getGson().toJson( permission ) ) );
            } );
        }
    }

    @Override
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
        dataSource.saveGroup( this );
    }

    @Override
    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
        dataSource.saveGroup( this );
    }

    public void addParent(String parent)
    {
        getParents().add( parent );
        computeEffectiveBungeePermissions();
        computeEffectiveBukkitPermissions();
        dataSource.saveGroup( this );
        dataSource.getPlayerCache().values().stream().filter( playerData ->
                playerData.getGroupName().equalsIgnoreCase( getGroupName() ) )
                .forEach( PlayerData::computeEffectiveBungeePermissions );
    }


    public void addParents(List<String> parents)
    {
        getParents().addAll( parents );
        computeEffectiveBungeePermissions();
        computeEffectiveBukkitPermissions();
        dataSource.saveGroup( this );
        dataSource.getPlayerCache().values().stream().filter( playerData ->
                playerData.getGroupName().equalsIgnoreCase( getGroupName() ) )
                .forEach( PlayerData::computeEffectiveBungeePermissions );
    }

    private Map<String, Permission> computeEffectiveBungeePermissions()
    {
        if ( !getParents().isEmpty() )
        {
            Map<String, Permission> tempMap = new HashMap<>();
            getParents().forEach( group -> {
                Data tempGroup = dataSource.getGroup( group );
                tempGroup.getBungeePermissions().entrySet().stream().filter( entry ->
                        entry.getValue().getValue() ).forEach( entry ->
                        tempMap.put( entry.getKey(), entry.getValue() ) );
            } );
            tempMap.putAll( getBungeePermissions() );
            this.effectiveBungeePermissions = tempMap;
            return tempMap;
        } else
        {
            this.effectiveBungeePermissions = getBungeePermissions();
            return getBungeePermissions();
        }
    }

    private Map<String, Permission> computeEffectiveBukkitPermissions()
    {
        if ( !getParents().isEmpty() )
        {
            Map<String, Permission> tempMap = new HashMap<>();
            getParents().forEach( group -> {
                Data tempGroup = dataSource.getGroup( group );
                tempGroup.getBukkitPermissions().entrySet().stream().filter( entry ->
                        entry.getValue().getValue() ).forEach( entry ->
                        tempMap.put( entry.getKey(), entry.getValue() ) );
            } );
            tempMap.putAll( getBukkitPermissions() );
            this.effectiveBukkitPermissions = tempMap;
            return tempMap;

        } else
        {
            this.effectiveBukkitPermissions = getBukkitPermissions();
            return getBukkitPermissions();
        }
    }
}
