package com.hybris.datahub.api.event;

import com.hybris.datahub.runtime.domain.PublicationRetry;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Deprecated(since = "6.4", forRemoval = true)
public class DeletePublicationRetryEvent extends PublicationEvent
{
    private final Collection<PublicationRetry> publicationRetries;
    private final long targetSystemId;


    public DeletePublicationRetryEvent(long poolId, long targetSystemId, Collection<PublicationRetry> retries)
    {
        super(poolId);
        this.targetSystemId = targetSystemId;
        this.publicationRetries = (retries != null) ? retries : Collections.<PublicationRetry>emptyList();
    }


    public Collection<PublicationRetry> getPublicationRetries()
    {
        return this.publicationRetries;
    }


    public long getTargetSystemId()
    {
        return this.targetSystemId;
    }


    public boolean equals(Object o)
    {
        if(o instanceof DeletePublicationRetryEvent)
        {
            DeletePublicationRetryEvent that = (DeletePublicationRetryEvent)o;
            return (getPoolId() == that.getPoolId() && this.targetSystemId == that.targetSystemId && this.publicationRetries
                            .equals(that.publicationRetries));
        }
        return false;
    }


    public int hashCode()
    {
        return (new HashCodeBuilder())
                        .append(getPoolId())
                        .append(this.targetSystemId)
                        .append(this.publicationRetries)
                        .build().intValue();
    }


    public String toString()
    {
        return (new ToStringBuilder(this)).toString();
    }
}
