package com.hybris.datahub.api.event;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.concurrent.Immutable;

@Immutable
public class InitiatePublicationRetryEvent extends InitiatePublicationEvent
{
    private static final long serialVersionUID = -4647216038095176126L;
    private final ImmutableList<String> targetSystemNames;


    public InitiatePublicationRetryEvent(long poolId, List<String> targetSystemNames)
    {
        super(poolId, targetSystemNames);
        this.targetSystemNames = ImmutableList.copyOf(targetSystemNames);
    }


    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + this.targetSystemNames.hashCode();
        return result;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof InitiatePublicationRetryEvent))
        {
            return false;
        }
        if(!super.equals(o))
        {
            return false;
        }
        InitiatePublicationRetryEvent that = (InitiatePublicationRetryEvent)o;
        if(!this.targetSystemNames.equals(that.targetSystemNames))
        {
            return false;
        }
        return true;
    }


    public String toString()
    {
        return "InitiatePublicationRetryEvent{poolId=" + getPoolId() + ", targetSystemNames=" + this.targetSystemNames + "}";
    }
}
