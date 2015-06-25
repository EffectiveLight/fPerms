/*
 * Copyright (c) Effective Light 2015.
 * All rights reserved.
 */

package me.hamzaxx.fperms.bungee.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.hamzaxx.fperms.common.permissions.Permission;

import java.io.IOException;

public class PermissionTypeAdapter extends TypeAdapter<Permission>
{
    @Override
    public void write(JsonWriter jsonWriter, Permission permission) throws IOException
    {

    }

    @Override
    public Permission read(JsonReader jsonReader) throws IOException
    {
        return null;
    }
}
