package me.hamzaxx.fperms.bungee.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.regex.Pattern;

public class Util
{

    private static final Pattern STRIP_COLORS = Pattern.compile( "(?i)&[0-9A-FK-OR]" );

    public static String stripColors(String text)
    {
        return STRIP_COLORS.matcher( text ).replaceAll( "" );
    }

    public static void sendMessage(CommandSender commandSender, String message, Object... objects)
    {
        commandSender.sendMessage( TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes( '&', String.format( message, objects ) ) ) );
    }

    public static void sendError(CommandSender commandSender, String message, Object... objects)
    {
        commandSender.sendMessage( TextComponent.fromLegacyText( String.format( ChatColor.RED + message, objects ) ) );
    }

    public static void sendSuccess(CommandSender commandSender, String message, Object... objects)
    {
        String[] formattedArgs = new String[ objects.length ];
        String formattedMessage = "&2" + message;
        for ( int i = 0; i < objects.length; i++ )
        {
            formattedArgs[ i ] = "&a" + objects[ i ] + "&2";
        }
        commandSender.sendMessage( TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes( '&',
                        String.format( formattedMessage, ( Object[] ) formattedArgs ) ) ) );
    }
}
