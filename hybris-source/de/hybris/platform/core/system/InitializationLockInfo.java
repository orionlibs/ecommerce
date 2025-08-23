package de.hybris.platform.core.system;

import java.util.Date;

public class InitializationLockInfo
{
    private final String tenantId;
    private final int clusterNodeId;
    private final Date date;
    private final String processName;
    private final long instanceIdentifier;
    private final boolean locked;


    public InitializationLockInfo(String tenantId, int clusterNodeId, Date date, boolean locked, String processName, long instanceIdentifer)
    {
        this.processName = processName;
        this.tenantId = tenantId;
        this.clusterNodeId = clusterNodeId;
        this.date = date;
        this.locked = locked;
        this.instanceIdentifier = instanceIdentifer;
    }


    public String getTenantId()
    {
        return this.tenantId;
    }


    public int getClusterNodeId()
    {
        return this.clusterNodeId;
    }


    public Date getDate()
    {
        return this.date;
    }


    public boolean isLocked()
    {
        return this.locked;
    }


    public String getProcessName()
    {
        return this.processName;
    }


    public long getInstanceIdentifier()
    {
        return this.instanceIdentifier;
    }
}
