/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bukkit.listeners;

import com.google.gson.JsonParser;
import me.hamzaxx.fperms.bukkit.Permissions;
import me.hamzaxx.fperms.bukkit.data.PlayerData;
import me.hamzaxx.fperms.bukkit.fPermsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class MessageListener implements PluginMessageListener
{

    private final JsonParser PARSER = new JsonParser();

    @Override
    public void onPluginMessageReceived(String channel, Player ignored, byte[] message)
    {
        /*System.out.println( new String( message ) );
        try
        {
            System.out.println( new String( message, "UTF-8" ) );
        } catch ( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        try ( DataInputStream in = new DataInputStream( new ByteArrayInputStream( message ) ) )
        {
            String task = in.readUTF();
            Player player;
            String groupName;
            String groupPrefix;
            String groupSuffix;
            String playerPrefix;
            String playerSuffix;
            String permissionsJson;
            switch ( task )
            {
                case "sendPerms":
                    player = Bukkit.getPlayer( in.readUTF() );
                    if ( player.isOnline() )
                    {
                        groupName = in.readUTF();
                        groupPrefix = in.readUTF();
                        groupSuffix = in.readUTF();
                        playerPrefix = in.readUTF();
                        playerSuffix = in.readUTF();
                        permissionsJson = in.readUTF();
                        Permissions.getPlayerData().put( player.getName(), new PlayerData( groupName, groupPrefix,
                                groupSuffix, playerPrefix, playerSuffix, handlePerms( player, permissionsJson ) ) );
                    }
                    break;
                case "updateALL":
                    //player = Bukkit.getPlayer( in.readUTF() );
                    groupName = in.readUTF();
                    groupPrefix = in.readUTF();
                    groupSuffix = in.readUTF();
                    playerPrefix = in.readUTF();
                    playerSuffix = in.readUTF();
                    @SuppressWarnings("unchecked")
                    Map<String, Boolean> perms = fPermsPlugin.getGson().fromJson( in.readUTF(), Map.class );
                    Bukkit.getOnlinePlayers().stream().filter( players ->
                            Permissions.getPlayerData().get( players.getName() ).getGroupName().equals( groupName ) )
                            .forEach( players -> {
                                Permissions.clearPlayer( players );
                                Permissions.setPermissions( players, perms );
                                Permissions.getPlayerData().put( players.getName(),
                                        new PlayerData(  groupName, groupPrefix, groupSuffix, playerPrefix, playerSuffix,
                                                perms ) );
                            } );
                    break;
                case "updateGroupPrefix":
                    player = Bukkit.getPlayer( in.readUTF() );
                    if ( player.isOnline() )
                    {
                        groupPrefix = in.readUTF();
                        Permissions.getPlayerData().get( player.getName() ).setGroupPrefix( groupPrefix );
                    }
                    break;
                case "updateGroupSuffix":
                    player = Bukkit.getPlayer( in.readUTF() );
                    if ( player.isOnline() )
                    {
                        groupSuffix = in.readUTF();
                        Permissions.getPlayerData().get( player.getName() ).setGroupSuffix( groupSuffix );
                    }
                    break;
                case "updatePlayerPrefix":
                    player = Bukkit.getPlayer( in.readUTF() );
                    if ( player.isOnline() )
                    {
                        playerPrefix = in.readUTF();
                        Permissions.getPlayerData().get( player.getName() ).setPlayerPrefix( playerPrefix );
                    }
                    break;
                case "updatePlayerSuffix":
                    player = Bukkit.getPlayer( in.readUTF() );
                    if ( player.isOnline() )
                    {
                        playerSuffix = in.readUTF();
                        Permissions.getPlayerData().get( player.getName() ).setPlayerPrefix( playerSuffix );
                    }
                    break;
                case "setPermission":
                    player = Bukkit.getPlayer( in.readUTF() );
                    if ( player.isOnline() )
                    {
                        String addPermission = in.readUTF();
                        boolean value = in.readBoolean();
                        System.out.println( addPermission + " " + value );
                        Permissions.setPermission( player, addPermission, value );
                        Permissions.getPlayerData().get( player.getName() ).setPermission( addPermission, value );
                    }
                    break;
                case "unsetPermission":
                    player = Bukkit.getPlayer( in.readUTF() );
                    if ( player.isOnline() )
                    {
                        String removePermission = in.readUTF();
                        Permissions.unsetPermission( player, removePermission );
                        Permissions.getPlayerData().get( player.getName() ).getEffectivePermissions().remove(
                                removePermission );
                    }
                    break;
                case "updatePlayerPermissions":
                    player = Bukkit.getPlayer( in.readUTF() );
                    @SuppressWarnings("unchecked")
                    Map<String, Boolean> permissions = fPermsPlugin.getGson().fromJson( in.readUTF(), Map.class );
                    if ( player.isOnline() )
                    {
                        Permissions.clearPlayer( player );
                        Permissions.setPermissions( player, permissions );
                        Permissions.getPlayerData().get( player.getName() ).getEffectivePermissions().putAll(
                                permissions );
                    }
                    break;
            }
        } catch ( IOException e )
        {
            e.printStackTrace();
        }*/
    }

    @SuppressWarnings("all")
    private Map<String, Boolean> handlePerms(Player player, String json)
    {
        Map<String, Boolean> permissions = fPermsPlugin.getGson().fromJson( json, Map.class );
        permissions.entrySet().forEach( entry -> {
            Permissions.setPermission( player, entry.getKey(), entry.getValue() );
        } );
        return permissions;
    }
}
