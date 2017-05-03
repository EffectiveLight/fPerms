package me.hamzaxx.fperms.common.netty;

public class ClientHello
{

    private String serverName;

    public ClientHello(String serverName)
    {
        this.serverName = serverName;
    }

    public String getServerName()
    {
        return serverName;
    }
}
