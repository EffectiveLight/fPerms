/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

import com.google.gson.Gson;
import me.hamzaxx.fperms.bukkit.data.PlayerData;
import me.hamzaxx.fperms.common.netty.Change;
import me.hamzaxx.fperms.common.netty.ChangeType;
import me.hamzaxx.fperms.common.permissions.Permission;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Test
{

    private Gson gson = new Gson();

    @org.junit.Test
    public void testSerialization()
    {
        Map<String, PlayerData> list = new HashMap<>();
        Map<String, Permission> perms = new HashMap<>();
        list.put( "hamzaxx", new PlayerData( null, "Mod", "", "", perms ) );
        list.put( "effective_light", new PlayerData( null, "Admin", "", "", perms ) );
        //System.out.println( list );
        File file = new File( "dir" );
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
                        System.out.println( playerData.getName() );
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

    @org.junit.Test
    public void testJson()
    {
        Change change = new Change( ChangeType.GROUP, "changename", "data" );
        String json = gson.toJson( change );
        System.out.println( json );
        Change backToChange = gson.fromJson( json, Change.class );
        System.out.println( backToChange.getData() );
    }
}
