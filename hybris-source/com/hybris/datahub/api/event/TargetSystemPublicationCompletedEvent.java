package com.hybris.datahub.api.event;

import com.hybris.datahub.api.publication.PublicationResult;
import javax.annotation.concurrent.Immutable;

@Immutable
public class TargetSystemPublicationCompletedEvent extends PublicationEvent
{
    private static final long serialVersionUID = 5401657770915700363L;
    private final long publicationId;
    private int errorCount;
    private String completionStatus;
    private PublicationResult publicationResult;


    public TargetSystemPublicationCompletedEvent(long poolId, long publicationId)
    {
        super(poolId);
        this.publicationId = publicationId;
    }


    public TargetSystemPublicationCompletedEvent(long poolId, long publicationId, int errorCount, String publicationStatus)
    {
        this(poolId, publicationId);
        this.errorCount = errorCount;
        this.completionStatus = publicationStatus;
    }


    public TargetSystemPublicationCompletedEvent(long poolId, long publicationId, int errorCount, String publicationStatus, PublicationResult publicationResult)
    {
        this(poolId, publicationId);
        this.errorCount = errorCount;
        this.completionStatus = publicationStatus;
        this.publicationResult = publicationResult;
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
        if(!(o instanceof TargetSystemPublicationCompletedEvent))
        {
            return false;
        }
        if(!super.equals(o))
        {
            return false;
        }
        TargetSystemPublicationCompletedEvent that = (TargetSystemPublicationCompletedEvent)o;
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


    public int getErrorCount()
    {
        return this.errorCount;
    }


    public String getCompletionStatus()
    {
        return this.completionStatus;
    }


    public PublicationResult getPublicationResult()
    {
        return this.publicationResult;
    }


    public String toString()
    {
        return "TargetSystemPublicationCompletedEvent{publicationId=" + this.publicationId + ", poolId=" +
                        getPoolId() + ", publicationResult=" +
                        getPublicationResult() + "}";
    }
}
