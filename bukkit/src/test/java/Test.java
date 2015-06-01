/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

import me.hamzaxx.fperms.bukkit.data.PlayerData;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Test
{

    @org.junit.Test
    public void testSerialization()
    {
        Map<String, PlayerData> list = new HashMap<>();
        Map<String, Boolean> perms = new HashMap<>();
        list.put( "hamzaxx", new PlayerData( null, "Mod", "&6Mod", "&8 >&f", "", "", perms ) );
        list.put( "effective_light", new PlayerData( null, "Admin", "&cAdmin", "&8 >&f", "", "", perms ) );
        //System.out.println( list );
        File file = new File( "C:\\Users\\hamza\\Desktop\\bungee\\test.dat" );
        try
        {
            System.out.println( file.length() );
            if ( !file.exists() )
            {
                if ( file.createNewFile() )
                {
                    System.out.println( "File created!" );
                }
            }

            try ( ObjectOutputStream outputStream = new ObjectOutputStream( new FileOutputStream( file ) ) )
            {
                outputStream.writeObject( list );
            }

            if ( file.length() != 0 )
            {
                try ( ObjectInputStream in = new ObjectInputStream( new FileInputStream( file ) ) )
                {
                    @SuppressWarnings("unchecked")
                    Map<String, PlayerData> listPersisted = ( HashMap ) in.readObject();

                    listPersisted.forEach( (playerName, playerData) -> {
                        System.out.println( playerName );
                        System.out.println( playerData.getGroupName() );
                    } );
                }
            } else
            {
                System.out.println( "Data not persisted!" );
            }
            new PrintWriter( file ).close();
            System.out.println( file.length() );
        } catch ( IOException | ClassNotFoundException e )
        {
            e.printStackTrace();
        }

    }

}
