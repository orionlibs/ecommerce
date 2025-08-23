package com.hybris.datahub.api.event;

import javax.annotation.concurrent.Immutable;

@Immutable
public class DataLoadingCompletedEvent extends DataLoadingEvent implements ProcessCompletedEvent
{
    private static final long serialVersionUID = 7988639996013886087L;
    private final String status;
    private final long feedId;
    private final long itemCount;


    public DataLoadingCompletedEvent(long actionId, long poolId, long feedId, long itemCount, String status)
    {
        super(poolId, actionId);
        this.status = status;
        this.feedId = feedId;
        this.itemCount = itemCount;
    }


    public String toString()
    {
        return "DataLoadingCompletedEvent{actionId=" + getActionId() + ", feedId=" + this.feedId + ", poolId=" +
                        getPoolId() + ", itemCount=" + this.itemCount + ", status='" + this.status + "'}";
    }


    public long getFeedId()
    {
        return this.feedId;
    }


    public long getItemCount()
    {
        return this.itemCount;
    }


    public String getStatus()
    {
        return this.status;
    }
}
