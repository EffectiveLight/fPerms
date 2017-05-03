package me.hamzaxx.fperms.common.netty;

public class ClientBye
{
    private String serverName;

    public ClientBye(String serverName)
    {
        this.serverName = serverName;
    }

    public String getServerName()
    {
        return serverName;
    }
}
