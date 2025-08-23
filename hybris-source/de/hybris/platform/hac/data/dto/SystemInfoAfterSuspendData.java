package de.hybris.platform.hac.data.dto;

import de.hybris.platform.core.suspend.RunningThread;
import java.util.List;

public class SystemInfoAfterSuspendData
{
    private String resumeResource;
    private String suspendToken;
    private String resumeToken;
    private String errorMessage;
    private String systemStatus;
    private Boolean forShutdown;
    private List<RunningThread> runningOperations;


    public String getResumeResource()
    {
        return this.resumeResource;
    }


    public void setResumeResource(String resumeResource)
    {
        this.resumeResource = resumeResource;
    }


    public String getSuspendToken()
    {
        return this.suspendToken;
    }


    public void setSuspendToken(String suspendToken)
    {
        this.suspendToken = suspendToken;
    }


    public String getResumeToken()
    {
        return this.resumeToken;
    }


    public void setResumeToken(String resumeToken)
    {
        this.resumeToken = resumeToken;
    }


    public String getErrorMessage()
    {
        return this.errorMessage;
    }


    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }


    public String getSystemStatus()
    {
        return this.systemStatus;
    }


    public void setSystemStatus(String systemStatus)
    {
        this.systemStatus = systemStatus;
    }


    public Boolean getForShutdown()
    {
        return this.forShutdown;
    }


    public void setForShutdown(Boolean forShutdown)
    {
        this.forShutdown = forShutdown;
    }


    public List<RunningThread> getRunningOperations()
    {
        return this.runningOperations;
    }


    public void setRunningOperations(List<RunningThread> runningOperations)
    {
        this.runningOperations = runningOperations;
    }
}
