package de.hybris.platform.hac.data.dto;

public class SuspendResumeOptions
{
    private String resumeToken;
    private String suspendToken;
    private Boolean forShutdown;


    public String getResumeToken()
    {
        return this.resumeToken;
    }


    public void setResumeToken(String resumeToken)
    {
        this.resumeToken = resumeToken;
    }


    public String getSuspendToken()
    {
        return this.suspendToken;
    }


    public void setSuspendToken(String suspendToken)
    {
        this.suspendToken = suspendToken;
    }


    public Boolean isForShutdown()
    {
        return (this.forShutdown == null) ? Boolean.FALSE : this.forShutdown;
    }


    public void setForShutdown(Boolean forShutdown)
    {
        this.forShutdown = forShutdown;
    }
}
