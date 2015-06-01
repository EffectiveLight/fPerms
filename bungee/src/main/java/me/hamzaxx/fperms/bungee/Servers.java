/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee;

import me.hamzaxx.fperms.bungee.data.Data;
import me.hamzaxx.fperms.bungee.data.DataSource;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Servers
{

    private static DataSource dataSource;

    public Servers(fPermsPlugin plugin)
    {
        dataSource = plugin.getDataSource();
    }

    public static void updateAll()
    {
        try ( ByteArrayOutputStream b = new ByteArrayOutputStream();
              DataOutputStream out = new DataOutputStream( b ) )
        {
            for ( Data group : dataSource.getGroups().values() )
            {
                for ( ServerInfo serverInfo : ProxyServer.getInstance().getServers().values() )
                {
                    if ( serverInfo.getPlayers().size() != 0 )
                    {
                        out.writeUTF( "updateAll" );
                        out.writeUTF( group.getGroupName() );
                        out.writeUTF( group.getPrefix() );
                        out.writeUTF( group.getSuffix() );
                        out.writeUTF( group.getEffectiveBukkitPermissionsJson() );
                        serverInfo.sendData( "fPermsPlugin", b.toByteArray() );
                    }
                }
            }
        } catch ( IOException e )
        {
            ProxyServer.getInstance().getLogger().severe( e.getMessage() );
        }
    }

    public static void updatePlayer(ProxiedPlayer player)
    {
        try ( ByteArrayOutputStream b = new ByteArrayOutputStream();
              DataOutputStream out = new DataOutputStream( b ) )
        {
            Data group = dataSource.getPlayerGroup( player.getUniqueId() );
            Data playerData = dataSource.getPlayerData( player.getUniqueId() );
            out.writeUTF( "updatePlayer" );
            out.writeUTF( player.getName() );
            out.writeUTF( group.getGroupName() );
            out.writeUTF( group.getPrefix() );
            out.writeUTF( group.getSuffix() );
            out.writeUTF( playerData.getPrefix() );
            out.writeUTF( playerData.getSuffix() );
            out.writeUTF( group.getEffectiveBukkitPermissionsJson() );
            player.getServer().sendData( "fPermsPlugin", b.toByteArray() );
        } catch ( IOException e )
        {
            ProxyServer.getInstance().getLogger().severe( e.getMessage() );
        }
    }

    public static void updateGroupPrefix(String groupName)
    {
        try ( ByteArrayOutputStream b = new ByteArrayOutputStream();
              DataOutputStream out = new DataOutputStream( b ) )
        {
            Data group = dataSource.getGroup( groupName );
            for ( ServerInfo serverInfo : ProxyServer.getInstance().getServers().values() )
            {
                out.writeUTF( "updatePrefix" );
                out.writeUTF( group.getGroupName() );
                out.writeUTF( group.getPrefix() );
                serverInfo.sendData( "fPermsPlugin", b.toByteArray() );
            }
        } catch ( IOException e )
        {
            ProxyServer.getInstance().getLogger().severe( e.getMessage() );
        }
    }

    public static void updateGroupSuffix(String groupName)
    {
        try ( ByteArrayOutputStream b = new ByteArrayOutputStream();
              DataOutputStream out = new DataOutputStream( b ) )
        {
            Data group = dataSource.getGroup( groupName );
            for ( ServerInfo serverInfo : ProxyServer.getInstance().getServers().values() )
            {
                out.writeUTF( "updateSuffix" );
                out.writeUTF( group.getGroupName() );
                out.writeUTF( group.getSuffix() );
                serverInfo.sendData( "fPermsPlugin", b.toByteArray() );
            }
        } catch ( IOException e )
        {
            ProxyServer.getInstance().getLogger().severe( e.getMessage() );
        }
    }

    public static void setGroupPermission(String groupName, String perm, boolean value)
    {
        try ( ByteArrayOutputStream b = new ByteArrayOutputStream();
              DataOutputStream out = new DataOutputStream( b ) )
        {
            Data groupData = dataSource.getGroup( groupName );
            for ( ServerInfo serverInfo : ProxyServer.getInstance().getServers().values() )
            {
                out.writeUTF( "setGroupPermission" );
                out.writeUTF( groupData.getGroupName() );
                out.writeUTF( perm );
                out.writeBoolean( value );
                serverInfo.sendData( "fPermsPlugin", b.toByteArray() );
            }
        } catch ( IOException e )
        {
            ProxyServer.getInstance().getLogger().severe( e.getMessage() );
        }
    }

    public static void unSetGroupPermission(String groupName, String perm)
    {
        try ( ByteArrayOutputStream b = new ByteArrayOutputStream();
              DataOutputStream out = new DataOutputStream( b ) )
        {
            for ( ProxiedPlayer player : ProxyServer.getInstance().getPlayers() )
            {
                Data group = dataSource.getPlayerGroup( player.getUniqueId() );
                if ( group.getGroupName().equals( groupName.toLowerCase() ) )
                {
                    out.writeUTF( "unSetPermission" );
                    out.writeUTF( group.getGroupName() );
                    out.writeUTF( perm );
                    player.getServer().sendData( "fPermsPlugin", b.toByteArray() );
                }
            }
        } catch ( IOException e )
        {
            ProxyServer.getInstance().getLogger().severe( e.getMessage() );
        }
    }

}

