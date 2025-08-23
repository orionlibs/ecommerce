package com.hybris.datahub.api.event;

public abstract class PublicationActionEvent extends PublicationEvent
{
    private final long actionId;


    public PublicationActionEvent(long poolId, long actionId)
    {
        super(poolId);
        this.actionId = actionId;
    }


    public long getActionId()
    {
        return this.actionId;
    }


    public boolean equals(Object o)
    {
        if(o != null && getClass().isInstance(o))
        {
            PublicationActionEvent that = (PublicationActionEvent)o;
            return (getPoolId() == that.getPoolId() && getActionId() == that.getActionId());
        }
        return false;
    }


    public int hashCode()
    {
        return 31 * super.hashCode() + (int)(this.actionId ^ this.actionId >>> 32L);
    }
}
