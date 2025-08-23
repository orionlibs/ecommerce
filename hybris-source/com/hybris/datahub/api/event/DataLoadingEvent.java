package com.hybris.datahub.api.event;

public abstract class DataLoadingEvent extends PoolEvent implements ActionEvent
{
    private long actionId;


    public DataLoadingEvent(long poolId, long actionId)
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
        return "DataLoad #" + this.actionId;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(super.equals(o))
        {
            DataLoadingEvent that = (DataLoadingEvent)o;
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
