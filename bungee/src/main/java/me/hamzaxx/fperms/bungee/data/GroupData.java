/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.data;

import com.google.gson.annotations.Expose;
import me.hamzaxx.fperms.bungee.util.MapMaker;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static java.util.concurrent.ConcurrentMap.Entry;

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
    private ConcurrentMap<String, ConcurrentMap<String, Boolean>> bungeePermissions;
    @Expose
    private ConcurrentMap<String, ConcurrentMap<String, Boolean>> bukkitPermissions;
    private ConcurrentMap<String, ConcurrentMap<String, Boolean>> effectiveBungeePermissions;
    private ConcurrentMap<String, ConcurrentMap<String, Boolean>> effectiveBukkitPermissions;

    public GroupData(DataSource dataSource, String name, String prefix, String suffix, List<String> parents,
                     ConcurrentMap<String, ConcurrentMap<String, Boolean>> bungeePermissions, ConcurrentMap<String,
            ConcurrentMap<String, Boolean>> bukkitPermissions)
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
    public ConcurrentMap<String, ConcurrentMap<String, Boolean>> getBukkitPermissions()
    {
        return bukkitPermissions;
    }

    @Override
    public List<String> getParents()
    {
        return parents;
    }

    @Override
    public ConcurrentMap<String, ConcurrentMap<String, Boolean>> getBungeePermissions()
    {
        return bungeePermissions;
    }


    @Override
    public ConcurrentMap<String, ConcurrentMap<String, Boolean>> getEffectiveBungeePermissions()
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
    public ConcurrentMap<String, ConcurrentMap<String, Boolean>> getEffectiveBukkitPermissions()
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
    public String getEffectiveBukkitPermissionsJson()
    {
        throw new UnsupportedOperationException( "Not applicable!" );
    }

    @Override
    public boolean unsetBungeePermission(String location, String permission)
    {
        if ( bungeePermissions.containsKey( location ) &&
                bungeePermissions.get( location ).containsKey( permission ) )
        {
            if ( getBungeePermissions().get( location ).get( permission ) )
            {
                dataSource.getGroups().values().stream().filter( parent ->
                        parent.getParents().contains( getGroupName() ) )
                        .filter( parent -> parent.getEffectiveBungeePermissions().get( location ) != null ).forEach(
                        parent -> parent.getEffectiveBukkitPermissions().remove( permission ) );
            }
            getEffectiveBungeePermissions().remove( permission );
            getBungeePermissions().remove( permission );
            dataSource.saveGroup( this );
            dataSource.getPlayerCache().values().stream().filter( playerData ->
                    playerData.getGroupName().equalsIgnoreCase( getGroupName() ) ).forEach(
                    PlayerData::computeEffectivePlayerBungeePermissions );
            return true;
        }
        return false;
    }

    @Override
    public boolean unsetBukkitPermission(String location, String permission)
    {
        if ( getBukkitPermissions().get( location ).containsKey( permission ) )
        {
            if ( getBungeePermissions().get( location ).get( permission ) )
            {
                dataSource.getGroups().values().stream().filter( parent ->
                        parent.getParents().contains( getGroupName() ) )
                        .filter( parent -> parent.getEffectiveBukkitPermissions().get( location ) != null ).forEach(
                        parent -> parent.getEffectiveBukkitPermissions().remove( permission ) );
            }
            getEffectiveBungeePermissions().remove( permission );
            getBungeePermissions().remove( permission );
            dataSource.saveGroup( this );
            dataSource.getPlayerCache().values().stream().filter( playerData ->
                    playerData.getGroupName().equalsIgnoreCase( getGroupName() ) ).forEach( playerData -> {
                playerData.computeEffectivePlayerBukkitPermissions();
                playerData.computeEffectiveBukkitPermissionsJson();
            } );
            return true;
        }
        return false;
    }

    @Override
    public void setBungeePermission(String location, String permission, boolean value)
    {
        getBungeePermissions().put( location,
                new MapMaker( getBukkitPermissions().get( location ) ).put( permission, value ).makeMap() );
        getEffectiveBukkitPermissions().put( location,
                new MapMaker( getEffectiveBukkitPermissions().get( location ) ).put( permission, value ).makeMap() );
        dataSource.saveGroup( this );
        if ( value )
        {
            dataSource.getGroups().values().stream().filter( group -> group.getParents().contains( getGroupName() ) )
                    .forEach( group ->
                            group.getEffectiveBungeePermissions().put( location, new MapMaker(
                                    group.getEffectiveBungeePermissions().get( location ) )
                                    .put( permission, true ).makeMap() ) );
        }
        dataSource.getPlayerCache().values().stream().filter( playerData ->
                playerData.getGroupName().equalsIgnoreCase( getGroupName() ) ).forEach(
                PlayerData::computeEffectivePlayerBungeePermissions );
    }

    @Override
    public void setBukkitPermission(String location, String permission, boolean value)
    {
        getBukkitPermissions().put( location,
                new MapMaker( getBukkitPermissions().get( location ) ).put( permission, value ).makeMap() );
        getEffectiveBukkitPermissions().put( location,
                new MapMaker( getEffectiveBukkitPermissions().get( location ) ).put( permission, value ).makeMap() );
        dataSource.saveGroup( this );
        if ( value )
        {
            dataSource.getGroups().values().stream().filter( group ->
                    group.getParents().contains( getGroupName() ) ).forEach( group ->
                group.getEffectiveBungeePermissions().put( location, new MapMaker(
                        group.getEffectiveBungeePermissions().get( location ) )
                        .put( permission, true ).makeMap() ));
        }
        dataSource.getPlayerCache().values().stream().filter( playerData ->
                playerData.getGroupName().equalsIgnoreCase( getGroupName() ) ).forEach( playerData -> {
            playerData.computeEffectivePlayerBukkitPermissions();
            playerData.computeEffectiveBukkitPermissionsJson();
        } );
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
            playerData.computeEffectivePlayerBukkitPermissions();
            playerData.computeEffectiveBukkitPermissionsJson();
            playerData.computeEffectivePlayerBungeePermissions();
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
            playerData.computeEffectivePlayerBukkitPermissions();
            playerData.computeEffectiveBukkitPermissionsJson();
            playerData.computeEffectivePlayerBungeePermissions();
        } );
    }

    private ConcurrentMap<String, ConcurrentMap<String, Boolean>> computeEffectiveBungeePermissions()
    {
        if ( !getParents().isEmpty() )
        {
            ConcurrentMap<String, ConcurrentMap<String, Boolean>> tempMap = new ConcurrentHashMap<>();
            getParents().forEach( group -> {
                Data tempGroup = dataSource.getGroup( group );
                tempGroup.getBungeePermissions().entrySet().forEach( entry ->
                        tempMap.put( entry.getKey(), entry.getValue().entrySet().stream().filter( Entry::getValue )
                                .collect( Collectors.toConcurrentMap( Entry::getKey, Entry::getValue ) ) ) );
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

    private ConcurrentMap<String, ConcurrentMap<String, Boolean>> computeEffectiveBukkitPermissions()
    {
        if ( !getParents().isEmpty() )
        {
            ConcurrentMap<String, ConcurrentMap<String, Boolean>> tempMap = new ConcurrentHashMap<>();
            getParents().forEach( group -> {
                Data tempGroup = dataSource.getGroup( group );
                tempGroup.getBukkitPermissions().entrySet().forEach( entry ->
                        tempMap.put( entry.getKey(), entry.getValue().entrySet().stream().filter( Entry::getValue )
                                .collect( Collectors.toConcurrentMap( Entry::getKey, Entry::getValue ) ) ) );
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
