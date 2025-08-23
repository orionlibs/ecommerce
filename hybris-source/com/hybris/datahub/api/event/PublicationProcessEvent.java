package com.hybris.datahub.api.event;

public abstract class PublicationProcessEvent extends PublicationEvent implements ProcessEvent
{
    private static final long serialVersionUID = -5673696254800508889L;
    private final long actionId;


    public PublicationProcessEvent(long poolId, long actionId)
    {
        super(poolId);
        this.actionId = actionId;
    }


    public long getActionId()
    {
        return this.actionId;
    }


    public String getProcessId()
    {
        return "Publication #" + this.actionId;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(super.equals(o))
        {
            PublicationProcessEvent that = (PublicationProcessEvent)o;
            return (this.actionId == that.actionId);
        }
        return false;
    }


    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (int)(this.actionId ^ this.actionId >>> 32L);
        return result;
    }
}
