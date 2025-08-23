package de.hybris.platform.hac.data.dto;

import de.hybris.platform.core.suspend.RunningThread;
import java.util.List;

public class SystemInfoData
{
    private String systemStatus;
    private List<RunningThread> runningOperations;


    public String getSystemStatus()
    {
        return this.systemStatus;
    }


    public void setSystemStatus(String systemStatus)
    {
        this.systemStatus = systemStatus;
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
