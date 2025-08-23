package com.hybris.datahub.api.event;

import com.google.common.base.Preconditions;
import com.hybris.datahub.model.CanonicalItem;
import java.util.Date;
import java.util.Objects;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Immutable
public class NoMorePublicationAttemptsEvent extends BaseCanonicalItemEvent
{
    private static final long serialVersionUID = -7255984234090418876L;
    private String targetSystemName;
    private Date currentTime;
    private int retryCount;


    public NoMorePublicationAttemptsEvent(CanonicalItem canonicalItem, String targetSystemName, int retryCount)
    {
        this(canonicalItem, targetSystemName, new Date(), retryCount);
    }


    public NoMorePublicationAttemptsEvent(CanonicalItem canonicalItem, String targetSystemName, Date currentTime, int retryCount)
    {
        super(canonicalItem);
        Preconditions.checkArgument((targetSystemName != null), "The target system name is required for building the publication retry attempt.");
        Preconditions.checkArgument((retryCount >= 0), "The retry count cannot be negative.");
        this.targetSystemName = targetSystemName;
        this.currentTime = (currentTime == null) ? new Date() : currentTime;
        this.retryCount = retryCount;
    }


    public long getCanonicalItemId()
    {
        return getCanonicalItem().getId().longValue();
    }


    public String getTargetSystemName()
    {
        return this.targetSystemName;
    }


    public Date getCurrentTime()
    {
        return this.currentTime;
    }


    public int getRetryCount()
    {
        return this.retryCount;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        NoMorePublicationAttemptsEvent that = (NoMorePublicationAttemptsEvent)o;
        return (getCanonicalItemId() == that.getCanonicalItemId() && this.retryCount == that.retryCount &&
                        Objects.equals(this.currentTime, that.currentTime) &&
                        Objects.equals(this.targetSystemName, that.targetSystemName));
    }


    public int hashCode()
    {
        return (new HashCodeBuilder())
                        .append(getCanonicalItemId())
                        .append(this.targetSystemName)
                        .append(this.currentTime)
                        .append(this.retryCount)
                        .build().intValue();
    }


    public String toString()
    {
        return "NoMorePublicationAttemptsEvent{canonicalItemId=" + getCanonicalItemId() + ", targetSystemName='" + this.targetSystemName + ", currentTime=" + this.currentTime + ", retryCount=" + this.retryCount + "}";
    }
}
