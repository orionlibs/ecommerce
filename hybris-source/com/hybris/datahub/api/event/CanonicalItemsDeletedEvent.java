package com.hybris.datahub.api.event;

import javax.annotation.concurrent.Immutable;

@Immutable
public class CanonicalItemsDeletedEvent extends PoolEvent
{
    private static final long serialVersionUID = -9149228577832903529L;
    private final long numberOfItemsArchived;
    private final long numberOfItemsDeleted;


    public CanonicalItemsDeletedEvent(Long poolId, long archivedCnt, long deletedCnt)
    {
        super(poolId.longValue());
        this.numberOfItemsArchived = archivedCnt;
        this.numberOfItemsDeleted = deletedCnt;
    }


    public long getNumberOfItemsArchived()
    {
        return this.numberOfItemsArchived;
    }


    public long getNumberOfItemsDeleted()
    {
        return this.numberOfItemsDeleted;
    }


    public String toString()
    {
        return "CanonicalItemsDeletedEvent{poolId=" + getPoolId() + ", numberOfItemsArchived=" + this.numberOfItemsArchived + ", numberOfItemsDeleted=" + this.numberOfItemsDeleted + "}";
    }
}
