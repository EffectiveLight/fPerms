/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.commands;

import me.hamzaxx.fperms.bungee.Servers;
import me.hamzaxx.fperms.bungee.data.Data;
import me.hamzaxx.fperms.bungee.data.DataSource;
import me.hamzaxx.fperms.bungee.fPermsPlugin;
import me.hamzaxx.fperms.bungee.util.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class fPermsCommand extends Command
{

    private String version;

    private DataSource dataSource;

    private final String[] HELP = new String[]{
            "&6&m---------------&a&m[&efPerms &4Version: &c" + version + "&a&m]&6&m-----------------",
            "&3/fPerms group &b<group> &3create",
            "&3/fPerms group &b<group> &3add &b<player>",
            "&3/fPerms group &b<group> &3set &b<bungee/bukkit> <permission> <value> [world/server] [world name/server name]",
            "&3/fPerms group &b<group> &3unset &b<bungee/bukkit> <permission> [world/server] [world name/server name]",
            "&3/fPerms group &b<group> &3prefix &b<prefix>",
            "&3/fPerms group &b<group> &3suffix &b<suffix>",
            "&3/fPerms group &b<group> &3parent &b<parents...>",
            "&3/fPerms player &b<player> &3set&b <bungee/bukkit> <permission> <value>",
            "&3/fPerms player &b<player> &3unset&b <bungee/bukkit> <permission>",
            "&3/fPerms player &b<player> &3prefix &b<prefix>",
            "&3/fPerms player &b<player> &3suffix &b<suffix>",
            "&6&m&n----------------------------------------" };

    public fPermsCommand(fPermsPlugin plugin)
    {
        super( "fPerms", "fperms.admin" );
        dataSource = plugin.getDataSource();
        version = plugin.getDescription().getVersion();
    }

    @Override
    public void execute(CommandSender commandSender, String[] args)
    {
        if ( args.length < 2 || args.length > 5 )
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
                            Servers.updatePlayer( player );
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
                    handleSet( commandSender, args[ 0 ], args[ 1 ], args[ 3 ], args[ 4 ], Boolean.parseBoolean( args[
                            5 ] ) );
                } else
                {
                    Util.sendMessage( commandSender,
                            "&3Usage: /fPerms <group/player> &b<group/player> &3set &b<permission> <value>" );
                }
                break;
            case "unset":
                if ( args.length == 5 )
                {
                    handleRemove( commandSender, args[ 0 ], args[ 1 ], args[ 3 ], args[ 4 ] );
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
                                // TODO: add server-sided refresh
                                Util.sendSuccess( commandSender, "Groups %s added as parents to %s!",
                                        Arrays.toString( parents )
                                                .replace( "[ ", "" ).replace( " ]", "" ), groupData.getGroupName() );
                            } else
                            {
                                if ( dataSource.groupExists( args[ 3 ] ) )
                                {
                                    groupData.addParent( args[ 3 ] );
                                    // TODO: add server-sided refresh
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

    private void handleSet(CommandSender commandSender, String task, String object, String option, String permission,
                           boolean value)
    {
        if ( task.equalsIgnoreCase( "group" ) )
        {
            if ( handleGroup( commandSender, object ) )
            {
                Data groupData = dataSource.getGroup( object );
                if ( option.equalsIgnoreCase( "bungee" ) )
                {
                   // groupData.setBungeePermission( permission, value );
                    // TODO: add server-sided refresh
                    Util.sendSuccess( commandSender, "Permission %s was set to %s for group %s on Bukkit!",
                            permission, value, groupData.getGroupName() );
                } else if ( object.equalsIgnoreCase( "bukkit" ) )
                {
                    // groupData.setBukkitPermission( permission, value );
                    // TODO: add server-sided refresh
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
                   // playerData.setBungeePermission( permission, value );
                    // TODO: add server-sided refresh
                    Util.sendSuccess( commandSender, "Permission %s was set to %s for player %s on BungeeCord!",
                            permission, value, player.getName() );
                } else if ( option.equalsIgnoreCase( "bukkit" ) )
                {
                    // playerData.setBukkitPermission( permission, value );
                    // TODO: add server-sided refresh
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
                    // groupData.unsetBungeePermission( permission );
                    // TODO: add server-sided refresh
                    Util.sendSuccess( commandSender, "Permission %s was unset for group %s on BungeeCord!",
                            permission, groupData.getGroupName() );
                } else if ( option.equalsIgnoreCase( "bukkit" ) )
                {
                    // groupData.unsetBukkitPermission( permission );
                    // TODO: add server-sided refresh
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
                    // playerData.unsetBungeePermission( permission );
                    // TODO: add server-sided refresh
                    Util.sendSuccess( commandSender, "Permission %s was unset for player %s on BungeeCord!",
                            permission, player.getName() );
                } else if ( option.equalsIgnoreCase( "bukkit" ) )
                {
                    // playerData.unsetBukkitPermission( permission );
                    // TODO: add server-sided refresh
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
                // TODO: add server-sided refresh
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
                // TODO: add server-sided refresh
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
                // TODO: add server-sided refresh
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
                // TODO: add server-sided refresh
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
        for ( int i = 0; i < 11; i++ )
        {
            TextComponent textComponent = new TextComponent( ChatColor.translateAlternateColorCodes( '&', HELP[ i ] ) );
            if ( commandSender instanceof ProxiedPlayer )
            {
                if ( i > 0 && i > 11 )
                {
                    textComponent.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND,
                            Util.stripColors( HELP[ i ] ) ) );
                }
            }
            commandSender.sendMessage( textComponent );
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
