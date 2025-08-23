package com.hybris.datahub.api.event;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.concurrent.Immutable;

@Immutable
public class InitiatePublicationEvent extends PublicationEvent
{
    private static final long serialVersionUID = -4647216038095176126L;
    private final ImmutableList<String> targetSystemNames;


    public InitiatePublicationEvent(long poolId, List<String> targetSystemNames)
    {
        super(poolId);
        this.targetSystemNames = ImmutableList.copyOf(targetSystemNames);
    }


    public ImmutableList<String> getTargetSystemNames()
    {
        return this.targetSystemNames;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof InitiatePublicationEvent))
        {
            return false;
        }
        if(!super.equals(o))
        {
            return false;
        }
        InitiatePublicationEvent that = (InitiatePublicationEvent)o;
        if(!this.targetSystemNames.equals(that.targetSystemNames))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + this.targetSystemNames.hashCode();
        return result;
    }


    public String toString()
    {
        return "InitiatePublicationEvent{poolId=" + getPoolId() + " ,targetSystemNames=" + this.targetSystemNames + "}";
    }
}
