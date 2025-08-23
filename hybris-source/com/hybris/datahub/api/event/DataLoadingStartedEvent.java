package com.hybris.datahub.api.event;

import javax.annotation.concurrent.Immutable;

@Immutable
public class DataLoadingStartedEvent extends DataLoadingEvent implements ProcessStartedEvent
{
    private static final long serialVersionUID = 7988639996013886087L;
    private long feedId;
    private long itemCount;


    public DataLoadingStartedEvent(long actionId, long poolId, long feedId, long itemCount)
    {
        super(poolId, actionId);
        this.feedId = feedId;
        this.itemCount = itemCount;
    }


    public String toString()
    {
        return "DataLoadingStartedEvent{actionId=" + getActionId() + ", feedId=" + this.feedId + ", poolId=" +
                        getPoolId() + ", itemCount=" + this.itemCount + "}";
    }


    public long getFeedId()
    {
        return this.feedId;
    }


    public long getItemCount()
    {
        return this.itemCount;
    }
}
