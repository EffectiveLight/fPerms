package me.hamzaxx.fperms.bungee.data;

import com.google.gson.annotations.Expose;
import me.hamzaxx.fperms.common.permissions.Permission;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData implements Data
{

    @Expose
    private UUID playerUUID;
    @Expose
    private String group;
    @Expose
    private String prefix;
    @Expose
    private String suffix;
    @Expose
    private Map<String, Permission> bungeePermissions;
    @Expose
    private Map<String, Permission> bukkitPermissions;

    private Map<String, Permission> effectiveBungeePermissions;
    private DataSource dataSource;

    public PlayerData(DataSource dataSource, UUID playerUUID,
                      String group, String prefix, String suffix,
                      Map<String, Permission> bungeePermissions, Map<String, Permission> bukkitPermissions)
    {
        this.dataSource = dataSource;
        this.playerUUID = playerUUID;
        this.group = group;
        this.prefix = prefix;
        this.suffix = suffix;
        this.bungeePermissions = bungeePermissions;
        this.bukkitPermissions = bukkitPermissions;
    }

    @Override
    public String getGroupName()
    {
        return group;
    }

    public void setGroupName(String groupName)
    {
        this.group = groupName;
        computeEffectiveBungeePermissions();
        dataSource.savePlayer( this );
    }

    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
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
    public Map<String, Permission> getBungeePermissions()
    {
        return bungeePermissions;
    }


    @Override
    public Map<String, Permission> getBukkitPermissions()
    {
        return bukkitPermissions;
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
        throw new UnsupportedOperationException( "unused" );
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
        dataSource.savePlayer( this );
    }

    @Override
    public boolean unsetBungeePermission(String permission)
    {
        if ( !getBungeePermissions().containsKey( permission ) ) return false;
        getBungeePermissions().remove( permission );
        computeEffectiveBungeePermissions();
        dataSource.savePlayer( this );
        return true;
    }

    @Override
    public boolean unsetBukkitPermission(String permission)
    {
        return false;
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

    public Map<String, Permission> computeEffectiveBungeePermissions()
    {
        Map<String, Permission> tempMap = new HashMap<>();
        tempMap.putAll( dataSource.getPlayerGroup( playerUUID ).getEffectiveBungeePermissions() );
        tempMap.putAll( getBungeePermissions() );
        this.effectiveBungeePermissions = tempMap;
        return tempMap;
    }

    public UUID getPlayerUUID()
    {
        return playerUUID;
    }
}
