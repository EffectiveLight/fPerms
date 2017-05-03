package me.hamzaxx.fperms.bungee.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand
{

    String name();

    String permission();

    String[] requiredArgs();

    String[] optionalArgs() default {};

}
