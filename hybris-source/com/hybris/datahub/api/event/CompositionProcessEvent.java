package com.hybris.datahub.api.event;

public abstract class CompositionProcessEvent extends CompositionEvent implements ProcessEvent
{
    private static final long serialVersionUID = -7255984234090418876L;
    private final long actionId;


    public CompositionProcessEvent(long poolId, long actionId)
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
        return "Composition #" + this.actionId;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(super.equals(o))
        {
            CompositionProcessEvent that = (CompositionProcessEvent)o;
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
