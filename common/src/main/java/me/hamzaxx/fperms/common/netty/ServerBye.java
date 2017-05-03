package me.hamzaxx.fperms.common.netty;

public class ServerBye
{
    private String reason;

    public ServerBye(String reason)
    {

        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }
}
