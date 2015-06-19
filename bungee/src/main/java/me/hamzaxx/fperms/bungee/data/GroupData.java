/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data;

import com.google.gson.annotations.Expose;
import me.hamzaxx.fperms.bungee.util.MapMaker;
import me.hamzaxx.fperms.shared.permissions.Location;
import me.hamzaxx.fperms.shared.permissions.Permission;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GroupData implements Data
{

    @Expose
    private String name;
    @Expose
    private String prefix;
    @Expose
    private String suffix;

    private DataSource dataSource;

    @Expose
    private List<String> parents;

    @Expose
    private ConcurrentMap<String, Permission> bungeePermissions;
    @Expose
    private ConcurrentMap<String, Permission> bukkitPermissions;
    private ConcurrentMap<String, Permission> effectiveBungeePermissions;
    private ConcurrentMap<String, Permission> effectiveBukkitPermissions;

    public GroupData(DataSource dataSource, String name, String prefix, String suffix, List<String> parents,
                     ConcurrentMap<String, Permission> bungeePermissions, ConcurrentMap<String,
            Permission> bukkitPermissions)
    {
        this.dataSource = dataSource;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.parents = parents;
        this.bungeePermissions = bungeePermissions;
        this.bukkitPermissions = bukkitPermissions;
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
    public ConcurrentMap<String, Permission> getBukkitPermissions()
    {
        return bukkitPermissions;
    }

    @Override
    public List<String> getParents()
    {
        return parents;
    }

    @Override
    public ConcurrentMap<String, Permission> getBungeePermissions()
    {
        return bungeePermissions;
    }


    @Override
    public ConcurrentMap<String, Permission> getEffectiveBungeePermissions()
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

   /* @Override
    public String getEffectiveBukkitPermissionsJson()
    {
        throw new UnsupportedOperationException( "Not applicable!" );
    }*/

    @Override
    public boolean unsetBungeePermission(String permission)
    {
        if ( bungeePermissions.containsKey( permission ) )
        {
            if ( getBungeePermissions().get( permission ).getValue() )
            {
                dataSource.getGroups().values().stream().filter( parent ->
                        parent.getParents().contains( getGroupName() ) ).forEach(
                        parent -> parent.getEffectiveBukkitPermissions().remove( permission ) );
            }
            getEffectiveBungeePermissions().remove( permission );
            getBungeePermissions().remove( permission );
            dataSource.saveGroup( this );
            dataSource.getPlayerCache().values().stream().filter( playerData ->
                    playerData.getGroupName().equalsIgnoreCase( getGroupName() ) ).forEach(
                    PlayerData::computeEffectiveBungeePermissions );
            return true;
        }
        return false;
    }

    @Override
    public boolean unsetBukkitPermission(String permission)
    {
        if ( getBukkitPermissions().containsKey( permission ) )
        {
            if ( getBungeePermissions().get( permission ).getValue() )
            {
                dataSource.getGroups().values().stream().filter( parent ->
                        parent.getParents().contains( getGroupName() ) ).forEach(
                        parent -> parent.getEffectiveBukkitPermissions().remove( permission ) );
            }
            getEffectiveBungeePermissions().remove( permission );
            getBungeePermissions().remove( permission );
            dataSource.saveGroup( this );
            dataSource.getPlayerCache().values().stream().filter( playerData ->
                    playerData.getGroupName().equalsIgnoreCase( getGroupName() ) ).forEach(
                    PlayerData::computeEffectiveBukkitPermissions );
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
            dataSource.getGroups().values().stream().filter( group -> group.getParents().contains( getGroupName() ) )
                    .forEach( group ->
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
                    group.getParents().contains( getGroupName() ) ).forEach( group ->
                    group.getEffectiveBungeePermissions().put( permission.getName(), permission ) );
        }
        dataSource.getPlayerCache().values().stream().filter( playerData ->
                playerData.getGroupName().equalsIgnoreCase( getGroupName() ) ).forEach(
                PlayerData::computeEffectiveBukkitPermissions );
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

    @Override
    public void addParent(String parent)
    {
        getParents().add( parent );
        computeEffectiveBungeePermissions();
        computeEffectiveBukkitPermissions();
        dataSource.saveGroup( this );
        dataSource.getPlayerCache().values().stream().filter( playerData ->
                playerData.getGroupName().equalsIgnoreCase( getGroupName() ) ).forEach( playerData -> {
            playerData.computeEffectiveBukkitPermissions();
            //playerData.computeEffectiveBukkitPermissionsJson();
            playerData.computeEffectiveBungeePermissions();
        } );
    }

    @Override
    public void addParents(List<String> parents)
    {
        getParents().addAll( parents );
        computeEffectiveBungeePermissions();
        computeEffectiveBukkitPermissions();
        dataSource.saveGroup( this );
        dataSource.getPlayerCache().values().stream().filter( playerData ->
                playerData.getGroupName().equalsIgnoreCase( getGroupName() ) ).forEach( playerData -> {
            playerData.computeEffectiveBukkitPermissions();
            //playerData.computeEffectiveBukkitPermissionsJson();
            playerData.computeEffectiveBungeePermissions();
        } );
    }

    private ConcurrentMap<String, Permission> computeEffectiveBungeePermissions()
    {
        if ( !getParents().isEmpty() )
        {
            ConcurrentMap<String, Permission> tempMap = new ConcurrentHashMap<>();
            getParents().forEach( group -> {
                Data tempGroup = dataSource.getGroup( group );
                tempGroup.getBungeePermissions().entrySet().forEach( entry ->
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

    private ConcurrentMap<String, Permission> computeEffectiveBukkitPermissions()
    {
        if ( !getParents().isEmpty() )
        {
            ConcurrentMap<String, Permission> tempMap = new ConcurrentHashMap<>();
            getParents().forEach( group -> {
                Data tempGroup = dataSource.getGroup( group );
                tempGroup.getBukkitPermissions().entrySet().forEach( entry ->
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
