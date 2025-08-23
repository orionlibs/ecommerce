package com.hybris.datahub.api.event;

import java.util.Date;

public class PerformDataRetentionCleanupEvent extends PoolEvent implements PerformProcessEvent
{
    protected Date currentDate;


    public PerformDataRetentionCleanupEvent(long poolId, Date currentDate)
    {
        super(poolId);
        this.currentDate = currentDate;
    }


    public Date getCurrentDate()
    {
        return this.currentDate;
    }


    public String getProcessId()
    {
        return "Pool Id: " + getPoolId();
    }


    public long getActionId()
    {
        return 0L;
    }


    public String toString()
    {
        return "PerformDataRetentionCleanupEvent{ poolId=" + getPoolId() + "}";
    }
}
