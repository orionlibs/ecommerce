package com.hybris.datahub.api.event;

import javax.annotation.concurrent.Immutable;

@Immutable
public class TargetSystemPublicationStartedEvent extends PublicationEvent
{
    private static final long serialVersionUID = 8768182157152236324L;
    private final long publicationId;


    public TargetSystemPublicationStartedEvent(long poolId, long publicationId)
    {
        super(poolId);
        this.publicationId = publicationId;
    }


    public long getPublicationId()
    {
        return this.publicationId;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof TargetSystemPublicationStartedEvent))
        {
            return false;
        }
        if(!super.equals(o))
        {
            return false;
        }
        TargetSystemPublicationStartedEvent that = (TargetSystemPublicationStartedEvent)o;
        if(this.publicationId != that.publicationId)
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (int)(this.publicationId ^ this.publicationId >>> 32L);
        return result;
    }


    public String toString()
    {
        return "TargetSystemPublicationStartedEvent{publicationId=" + this.publicationId + ", poolId=" +
                        getPoolId() + "}";
    }
}
