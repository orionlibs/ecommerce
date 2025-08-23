package com.hybris.datahub.api.publication;

import java.io.Serializable;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Immutable
public class PublicationResult implements Serializable
{
    private static final long serialVersionUID = 6856794769350840585L;
    private final long publicationId;
    private final String completionStatus;
    private final long successCount;
    private final long internalErrorCount;
    private final long externalErrorCount;
    private final long ignoredCount;


    public PublicationResult(long publicationId)
    {
        this(publicationId, null, 0L, 0L, 0L, 0L);
    }


    public PublicationResult(long publicationId, String completionStatus, long successCount, long internalErrorCount, long externalErrorCount, long ignoredCount)
    {
        this.publicationId = publicationId;
        this.completionStatus = completionStatus;
        this.successCount = successCount;
        this.internalErrorCount = internalErrorCount;
        this.externalErrorCount = externalErrorCount;
        this.ignoredCount = ignoredCount;
    }


    public long getPublicationId()
    {
        return this.publicationId;
    }


    public String getCompletionStatus()
    {
        return this.completionStatus;
    }


    public long getSuccessCount()
    {
        return this.successCount;
    }


    public long getInternalErrorCount()
    {
        return this.internalErrorCount;
    }


    public long getExternalErrorCount()
    {
        return this.externalErrorCount;
    }


    public long getIgnoredCount()
    {
        return this.ignoredCount;
    }


    public int hashCode()
    {
        return (new HashCodeBuilder())
                        .append(this.publicationId)
                        .append(this.completionStatus)
                        .append(this.successCount)
                        .append(this.internalErrorCount)
                        .append(this.externalErrorCount)
                        .append(this.ignoredCount)
                        .toHashCode();
    }


    public boolean equals(Object obj)
    {
        if(obj instanceof PublicationResult)
        {
            PublicationResult other = (PublicationResult)obj;
            return (new EqualsBuilder())
                            .append(this.publicationId, other.publicationId)
                            .append(this.completionStatus, other.completionStatus)
                            .append(this.successCount, other.successCount)
                            .append(this.internalErrorCount, other.internalErrorCount)
                            .append(this.externalErrorCount, other.externalErrorCount)
                            .append(this.ignoredCount, other.ignoredCount)
                            .isEquals();
        }
        return false;
    }


    public String toString()
    {
        return "PublicationResult{publicationId=" + this.publicationId + ", completionStatus=" + this.completionStatus + ", successCount=" + this.successCount + ", internalErrorCount=" + this.internalErrorCount + ", externalErrorCount=" + this.externalErrorCount + ", ignoredCount="
                        + this.ignoredCount + "}";
    }
}
