/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.commands;

import com.sun.istack.internal.Nullable;
import me.hamzaxx.fperms.bungee.annotations.SubCommand;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class SubCommandInfo
{

    private String name;
    private List<String> requiredArgs;
    private List<String> optionalArgs;
    private Method subCommandMethod;

    public SubCommandInfo(SubCommand subCommand, @Nullable Method subCommandMethod)
    {
        this.subCommandMethod = subCommandMethod;
        this.name = subCommand.name();
        this.requiredArgs = Arrays.asList( subCommand.requiredArgs() );
        this.optionalArgs = Arrays.asList( subCommand.optionalArgs() );
    }

    @Override
    public boolean equals(Object obj)
    {
        if ( obj instanceof SubCommandInfo )
        {
            SubCommandInfo subCommandInfo = ( SubCommandInfo ) obj;
            return requiredArgs.containsAll( subCommandInfo.getRequiredArgs() )
                    && optionalArgs.containsAll( subCommandInfo.getOptionalArgs() )
                    && name.equals( getName() );

        }
        return false;
    }

    public String getName()
    {
        return name;
    }

    public List<String> getOptionalArgs()
    {
        return optionalArgs;
    }

    public Method getSubCommandMethod()
    {
        return subCommandMethod;
    }

    public List<String> getRequiredArgs()
    {
        return requiredArgs;
    }

}
