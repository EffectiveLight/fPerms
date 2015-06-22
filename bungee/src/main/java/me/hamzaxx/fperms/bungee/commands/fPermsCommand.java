/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.commands;

import me.hamzaxx.fperms.bungee.data.Data;
import me.hamzaxx.fperms.bungee.data.DataSource;
import me.hamzaxx.fperms.bungee.data.GroupData;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import me.hamzaxx.fperms.bungee.util.Util;
import me.hamzaxx.fperms.shared.netty.Change;
import me.hamzaxx.fperms.shared.netty.ChangeType;
import me.hamzaxx.fperms.shared.permissions.Location;
import me.hamzaxx.fperms.shared.permissions.LocationType;
import me.hamzaxx.fperms.shared.permissions.Permission;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class fPermsCommand extends Command
{

    private DataSource dataSource;

    private final String[] HELP = new String[]{
            null,
            "&3/fPerms group &b<group> &3create",
            "&3/fPerms group &b<group> &3add &b<player>",
            "&3/fPerms group &b<group> &3set &bbungee <permission> <value> [server]",
            "&3/fPerms group &b<group> &3set &bbukkit <permission> <value> [world]",
            "&3/fPerms group &b<group> &3unset &b<bungee/bukkit> <permission>",
            "&3/fPerms group &b<group> &3prefix &b<prefix>",
            "&3/fPerms group &b<group> &3suffix &b<suffix>",
            "&3/fPerms group &b<group> &3parent &b<parents...>",
            "&3/fPerms player &b<player> &3set &bbungee <permission> <value> [server]",
            "&3/fPerms player &b<player> &3set &bbungee <permission> <value> [world]",
            "&3/fPerms group &b<group> &3unset &b<bungee/bukkit> <permission>",
            "&3/fPerms player &b<player> &3prefix &b<prefix>",
            "&3/fPerms player &b<player> &3suffix &b<suffix>",
            "&6&m&n-------------------------" };
    private fPermsPlugin plugin;

    public fPermsCommand(fPermsPlugin plugin)
    {
        super( "fPerms", "fperms.admin", "perms" );
        this.plugin = plugin;
        dataSource = plugin.getDataSource();
        HELP[ 0 ] = "&6&m---------------&a&m[&efPerms|Version:" + plugin.getDescription().getVersion() + "&a&m]&6&m-----------------";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args)
    {
        if ( args.length < 2 || args.length > 6 )
        {
            sendHelp( commandSender );
            return;
        }

        switch ( args[ 2 ].toLowerCase() )
        {
            case "add":
                if ( args.length == 4 )
                {
                    if ( args[ 0 ].equalsIgnoreCase( "group" ) )
                    {
                        ProxiedPlayer player = ProxyServer.getInstance().getPlayer( args[ 3 ] );
                        if ( player == null )
                        {
                            Util.sendError( commandSender, "That player isn't online!" );
                            return;
                        }
                        if ( handleGroup( commandSender, args[ 1 ] ) )
                        {
                            dataSource.setPlayerGroup( player, args[ 1 ] );
                            plugin.sendToServer( player.getServer(), new Change( ChangeType.GROUP_NAME, player.getName(), args[ 1 ] ) );
                            Util.sendSuccess( commandSender, "Player %s's group was set to %s!", player.getName(),
                                    args[ 1 ] );
                            Util.sendSuccess( player, "Your rank was set to %s!", args[ 1 ] );
                        }
                    } else
                    {
                        Util.sendMessage( commandSender, "&3Usage: /fPerms group &b<group> &3add &b<player>" );
                    }
                } else
                {
                    Util.sendMessage( commandSender, "&3Usage: /fPerms group &b<group> &3add &b<player>" );
                }
                break;
            case "create":
                if ( args.length == 3 )
                {
                    if ( args[ 0 ].equalsIgnoreCase( "group" ) )
                    {
                        if ( !handleGroup( commandSender, args[ 1 ] ) )
                        {
                            dataSource.addGroup( args[ 1 ] );
                            GroupData data = dataSource.getGroup( args[ 1 ] );
                            plugin.sentToAll( new Change( ChangeType.GROUP, data.getGroupName(), plugin.getGson().toJson( data ) ) );
                            Util.sendSuccess( commandSender, "Group %s was created!", args[ 1 ] );
                        } else
                        {
                            Util.sendError( commandSender, "That group already exists!" );
                        }
                    } else
                    {
                        Util.sendMessage( commandSender, "&3Usage: /fPerms group &b<group> &3create" );
                    }
                } else
                {
                    Util.sendMessage( commandSender, "&3Usage: /fPerms group &b<group> &3create" );
                }
                break;
            case "set":
                if ( args.length == 6 )
                {
                    handleSet( commandSender, args[ 0 ], args[ 1 ], args[ 2 ],
                            args[ 3 ], Boolean.parseBoolean( args[ 4 ] ), args[ 5 ] );
                } else if ( args.length == 5 )
                {
                    handleSet( commandSender, args[ 0 ], args[ 1 ], args[ 3 ],
                            args[ 4 ], Boolean.parseBoolean( args[ 5 ] ), null );
                } else
                {
                    Util.sendMessage( commandSender,
                            "&3Usage: /fPerms <group/player> &b<group/player> &3set &b<permission> <value> [server/world]" );
                }
                break;
            case "unset":
                if ( args.length == 5 )
                {
                    handleRemove( commandSender, args[ 0 ], args[ 1 ],
                            args[ 3 ], args[ 4 ] );
                } else
                {
                    Util.sendMessage( commandSender,
                            "&3Usage: /fPerms <group/player> &b<group/player> &3unset &b<permission> <value>" );
                }
                break;
            case "prefix":
                if ( args.length == 4 )
                {
                    handlePrefix( commandSender, args[ 0 ], args[ 1 ], args[ 3 ] );
                } else
                {
                    Util.sendMessage( commandSender,
                            "&3Usage: /fPerms <group/player> &b<group/player> &3prefix &b<prefix>" );
                }
                break;
            case "suffix":
                if ( args.length == 4 )
                {
                    handleSuffix( commandSender, args[ 0 ], args[ 1 ], args[ 3 ] );
                } else
                {
                    Util.sendMessage( commandSender,
                            "&3Usage: /fPerms <group/player> &b<group/player> &3suffix &b<suffix>" );
                }
                break;
            case "parent":
                if ( args.length == 4 )
                {
                    if ( args[ 0 ].equalsIgnoreCase( "group" ) )
                    {
                        if ( handleGroup( commandSender, args[ 1 ] ) )
                        {
                            Data groupData = dataSource.getGroup( args[ 1 ] );
                            if ( args[ 2 ].contains( "," ) )
                            {
                                String[] parents = args[ 3 ].split( "," );
                                List<String> groups = new CopyOnWriteArrayList<>();
                                for ( String parent : parents )
                                {
                                    if ( !dataSource.groupExists( parent ) )
                                    {
                                        Util.sendError( commandSender, "Group %s doesn't exist!", parent );
                                        return;
                                    }
                                    groups.add( parent );
                                }
                                groupData.addParents( groups );
                                plugin.sentToAll( new Change( ChangeType.REFRESH_PERMISSIONS, groupData.getGroupName(),
                                        plugin.getGson().toJson( groupData.getEffectiveBukkitPermissions() ) ) );
                                Util.sendSuccess( commandSender, "Groups %s added as parents to %s!",
                                        Arrays.toString( parents )
                                                .replace( "[ ", "" ).replace( " ]", "" ), groupData.getGroupName() );
                            } else
                            {
                                if ( dataSource.groupExists( args[ 3 ] ) )
                                {
                                    groupData.addParent( args[ 3 ] );
                                    plugin.sentToAll( new Change( ChangeType.REFRESH_PERMISSIONS, groupData.getGroupName(),
                                            plugin.getGson().toJson( groupData.getEffectiveBukkitPermissions() ) ) );
                                    Util.sendSuccess( commandSender, "Added group %s as a parent for %s!", args[ 3 ],
                                            groupData.getGroupName() );
                                } else
                                {
                                    Util.sendError( commandSender, "Parent group %s doesn't exist!", args[ 3 ] );
                                }
                            }
                        }
                    } else
                    {
                        Util.sendMessage( commandSender, "&3Usage: /fPerms group &b<group> &3parent &b<parents...>" );
                    }
                } else
                {
                    Util.sendMessage( commandSender, "&3Usage: /fPerms group &b<group> &3parent &b<parents...>" );
                }
                break;
            default:
                sendHelp( commandSender );
                break;
        }
    }

    private void handleSet(CommandSender commandSender, String task, String object,
                           String option, String permission, boolean value, String locationName)
    {
        if ( task.equalsIgnoreCase( "group" ) )
        {
            if ( handleGroup( commandSender, object ) )
            {
                Data groupData = dataSource.getGroup( object );
                if ( option.equalsIgnoreCase( "bungee" ) )
                {
                    if ( locationName == null )
                    {
                        groupData.setBungeePermission( new Permission( permission, new Location( LocationType.ALL ), value ) );
                    } else
                    {
                        groupData.setBungeePermission( new Permission( permission, new Location( LocationType.SERVER, locationName ), value ) );
                    }
                    Util.sendSuccess( commandSender, "Permission %s was set to %s for group %s on Bukkit!",
                            permission, value, groupData.getGroupName() );
                } else if ( object.equalsIgnoreCase( "bukkit" ) )
                {
                    Permission perm;
                    if ( locationName == null )
                    {
                        perm = new Permission( permission, new Location( LocationType.ALL ), value );
                        groupData.setBukkitPermission( perm );
                    } else
                    {
                        perm = new Permission( permission, new Location( LocationType.WORLD, locationName ), value );
                        groupData.setBukkitPermission( new Permission( permission, new Location( LocationType.WORLD, locationName ), value ) );
                    }
                    plugin.sentToAll( new Change( ChangeType.SET_GROUP_PERMISSION, groupData.getGroupName(), plugin.getGson().toJson( perm ) ) );
                    Util.sendSuccess( commandSender, "Permission %s was set to %s for group %s!",
                            permission, value, groupData.getGroupName() );
                } else
                {
                    Util.sendMessage( commandSender,
                            "&3/fPerms group &b<group> &3set &b<bungee/bukkit> <permission> <value>" );
                }
            }
        } else if ( task.equalsIgnoreCase( "player" ) )
        {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer( object );
            if ( handlePlayer( commandSender, player ) )
            {
                Data playerData = dataSource.getPlayerData( player.getUniqueId() );
                if ( option.equalsIgnoreCase( "bungee" ) )
                {
                    if ( locationName == null )
                    {
                        playerData.setBungeePermission( new Permission( permission, new Location( LocationType.ALL ), value ) );
                    } else
                    {
                        playerData.setBungeePermission( new Permission( permission, new Location( LocationType.SERVER, locationName ), value ) );
                    }

                    Util.sendSuccess( commandSender, "Permission %s was set to %s for player %s on BungeeCord!",
                            permission, value, player.getName() );
                } else if ( option.equalsIgnoreCase( "bukkit" ) )
                {
                    Permission perm;
                    if ( locationName == null )
                    {
                        perm = new Permission( permission, new Location( LocationType.ALL ), value );
                        playerData.setBukkitPermission( perm );
                    } else
                    {
                        perm = new Permission( permission, new Location( LocationType.WORLD, locationName ), value );
                        playerData.setBukkitPermission( perm );
                    }
                    plugin.sendToServer( player.getServer(),
                            new Change( ChangeType.SET_PLAYER_PERMISSION, player.getName(), plugin.getGson().toJson( perm ) ) );
                    Util.sendSuccess( commandSender, "Permission %s was set to %s for player %s on Bukkit!",
                            permission, value, player.getName() );
                } else
                {
                    Util.sendMessage( commandSender,
                            "&3/fPerms player &b<player> &3set &b<bungee/bukkit> <permission> <value>" );
                }
            }
        } else
        {
            Util.sendMessage( commandSender,
                    "&3/fPerms <group/player> &b<group/player> &3set &b<bungee/bukkit> <permission> <value>" );
        }
    }

    private void handleRemove(CommandSender commandSender, String task, String object, String option, String permission)
    {
        if ( task.equalsIgnoreCase( "group" ) )
        {
            if ( handleGroup( commandSender, object ) )
            {
                Data groupData = dataSource.getGroup( object );
                if ( option.equalsIgnoreCase( "bungee" ) )
                {
                    groupData.unsetBungeePermission( permission );
                    Util.sendSuccess( commandSender, "Permission %s was unset for group %s on BungeeCord!",
                            permission, groupData.getGroupName() );
                } else if ( option.equalsIgnoreCase( "bukkit" ) )
                {
                    groupData.unsetBukkitPermission( permission );
                    plugin.sentToAll( new Change( ChangeType.UNSET_GROUP_PERMISSION, groupData.getGroupName(), permission ) );
                    Util.sendSuccess( commandSender, "Permission %s was unset for group %s on Bukkit!", permission,
                            groupData.getGroupName() );
                } else
                {
                    Util.sendMessage( commandSender,
                            "&3/fPerms group &b<group> &3unset &b<bungee/bukkit> <permission>" );
                }
            }
        } else if ( task.equalsIgnoreCase( "player" ) )
        {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer( object );
            if ( handlePlayer( commandSender, player ) )
            {
                Data playerData = dataSource.getPlayerData( player.getUniqueId() );
                if ( option.equalsIgnoreCase( "bungee" ) )
                {
                    playerData.unsetBungeePermission( permission );
                    Util.sendSuccess( commandSender, "Permission %s was unset for player %s on BungeeCord!",
                            permission, player.getName() );
                } else if ( option.equalsIgnoreCase( "bukkit" ) )
                {
                    playerData.unsetBukkitPermission( permission );
                    plugin.sendToServer( player.getServer(), new Change( ChangeType.UNSET_PLAYER_PERMISSION, player.getName(), permission ) );
                    Util.sendSuccess( commandSender, "Permission %s was unset for player %s on Bukkit!", permission,
                            player.getName() );
                } else
                {
                    Util.sendMessage( commandSender,
                            "&3/fPerms player &b<player> &3unset &b<bungee/bukkit> <permission>" );
                }
            }
        } else
        {
            Util.sendMessage( commandSender,
                    "&3/fPerms <group/player> &b<group/player> &3unset &b<bungee/bukkit> <permission>" );
        }
    }

    private void handlePrefix(CommandSender commandSender, String task, String object, String prefix)
    {
        if ( task.equalsIgnoreCase( "group" ) )
        {
            if ( handleGroup( commandSender, object ) )
            {
                Data groupData = dataSource.getGroup( object );
                groupData.setPrefix( prefix );
                plugin.sentToAll( new Change( ChangeType.GROUP_PREFIX, groupData.getGroupName(), prefix ) );
                Util.sendSuccess( commandSender, "Prefix for group %s was set to %s!", groupData.getGroupName(),
                        prefix );
            }
        } else if ( task.equalsIgnoreCase( "player" ) )
        {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer( object );
            if ( handlePlayer( commandSender, player ) )
            {
                Data playerData = dataSource.getPlayerData( player.getUniqueId() );
                playerData.setPrefix( prefix );
                plugin.sendToServer( player.getServer(),
                        new Change( ChangeType.PLAYER_PREFIX, player.getName(), prefix ) );
                Util.sendSuccess( commandSender, "Prefix for player %s was set to %s!", player.getName(), prefix );
            }
        } else
        {
            Util.sendMessage( commandSender, "&3/fPerms <group/player> &b<group/player> &3prefix &b<prefix>" );
        }
    }

    private void handleSuffix(CommandSender commandSender, String task, String object, String suffix)
    {
        if ( task.equalsIgnoreCase( "group" ) )
        {
            if ( handleGroup( commandSender, object ) )
            {
                Data groupData = dataSource.getGroup( object );
                groupData.setSuffix( suffix );
                plugin.sentToAll( new Change( ChangeType.GROUP_SUFFIX, groupData.getGroupName(), suffix ) );
                Util.sendSuccess( commandSender, "Suffix for group %s was set to %s!", groupData.getGroupName(),
                        suffix );
            }
        } else if ( task.equalsIgnoreCase( "player" ) )
        {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer( object );
            if ( handlePlayer( commandSender, player ) )
            {
                Data playerData = dataSource.getPlayerData( player.getUniqueId() );
                playerData.setSuffix( suffix );
                plugin.sendToServer( player.getServer(),
                        new Change( ChangeType.PLAYER_SUFFIX, player.getName(), suffix ) );
                Util.sendSuccess( commandSender, "Suffix for player %s was set to %s!", player.getName(), suffix );
            }
        } else
        {
            Util.sendMessage( commandSender, "&3/fPerms <group/player> &b<group/player> &3suffix &b<suffix>" );
        }
    }

    private boolean handlePlayer(CommandSender commandSender, ProxiedPlayer player)
    {
        if ( player == null )
        {
            Util.sendError( commandSender, "That player isn't online!" );
            return false;
        }
        boolean playerExists = dataSource.playerExists( player.getUniqueId() );
        if ( !playerExists )
        {
            Util.sendError( commandSender, "That player doesn't exist!" );
            return false;
        }
        return true;
    }

    private void sendHelp(CommandSender commandSender)
    {
        for ( String help : HELP )
        {
            commandSender.sendMessage( TextComponent.fromLegacyText( ChatColor.translateAlternateColorCodes( '&', help ) ) );
        }
    }

    private boolean handleGroup(CommandSender commandSender, String groupName)
    {
        boolean groupExists = dataSource.groupExists( groupName );
        if ( !groupExists )
        {
            Util.sendError( commandSender, "That group doesn't exist!" );
        }
        return groupExists;
    }

}