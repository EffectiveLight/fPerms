/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.commands;

import me.hamzaxx.fperms.bungee.annotations.SubCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandExecutor extends Command
{

    private Map<String, SubCommandInfo> subCommandMap = new HashMap<>();

    private String name;

    public CommandExecutor(String name, Class<?>... subCommands)
    {
        super( name );
        this.name = name;
        for ( Class<?> subCommand : subCommands )
        {
            processCommandClass( subCommand );
        }
    }

    @Override
    public void execute(CommandSender commandSender, String[] args)
    {
        String name = args[ 2 ];
        List<String> allArgs = Arrays.asList( args );
        allArgs.remove( name );
        //  List<String> requiredArgs = allArgs.a

    }


    private void processCommandClass(Class<?> clazz)
    {
        for ( Method method : clazz.getMethods() )
        {
            SubCommand subCommand = method.getAnnotation( SubCommand.class );
            if ( subCommand != null )
            {
                // method.getPa
                //   method.getParameterAnnotations()
                subCommandMap.put( subCommand.name(), new SubCommandInfo( subCommand, method ) );
            }
        }
    }

}
