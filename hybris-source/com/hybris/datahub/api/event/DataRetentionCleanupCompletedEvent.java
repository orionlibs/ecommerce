package com.hybris.datahub.api.event;

public class DataRetentionCleanupCompletedEvent extends PoolEvent implements ProcessCompletedEvent
{
    public DataRetentionCleanupCompletedEvent(long poolId)
    {
        super(poolId);
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
        return "DataRetentionCleanupCompletedEvent{ poolId=" + getPoolId() + "}";
    }
}
