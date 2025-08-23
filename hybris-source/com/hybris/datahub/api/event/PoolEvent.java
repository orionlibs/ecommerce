package com.hybris.datahub.api.event;

import java.io.Serializable;

public abstract class PoolEvent extends DataHubEvent implements DataHubPoolEvent, Serializable
{
    private static final long serialVersionUID = -8804556686254896457L;
    private final long poolId;


    public PoolEvent(long poolId)
    {
        this.poolId = poolId;
    }


    public long getPoolId()
    {
        return this.poolId;
    }


    public boolean equals(Object o)
    {
        if(o != null && getClass().isInstance(o))
        {
            PoolEvent that = (PoolEvent)o;
            return (this.poolId == that.poolId);
        }
        return false;
    }


    public int hashCode()
    {
        return Long.valueOf(this.poolId).hashCode();
    }


    public String toString()
    {
        return "PoolEvent{poolId=" + this.poolId + "}";
    }
}
