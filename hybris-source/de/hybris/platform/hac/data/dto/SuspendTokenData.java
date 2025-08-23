package de.hybris.platform.hac.data.dto;

public class SuspendTokenData
{
    private String suspendToken;
    private boolean forShutdown;


    public String getSuspendToken()
    {
        return this.suspendToken;
    }


    public void setSuspendToken(String suspendToken)
    {
        this.suspendToken = suspendToken;
    }


    public boolean isForShutdown()
    {
        return this.forShutdown;
    }


    public void setForShutdown(boolean forShutdown)
    {
        this.forShutdown = forShutdown;
    }
}
