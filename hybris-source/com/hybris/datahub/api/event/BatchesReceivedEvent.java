package com.hybris.datahub.api.event;

import java.util.Date;
import java.util.Set;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Immutable
public class BatchesReceivedEvent extends MonitoringEvent
{
    private final String traceId;
    private final Set<String> batches;
    private final Date createdDate;
    private final String poolName;


    public BatchesReceivedEvent(String traceId, Set<String> batches, Date createdDate, String poolName)
    {
        this.traceId = traceId;
        this.batches = batches;
        this.createdDate = createdDate;
        this.poolName = poolName;
    }


    public String getTraceId()
    {
        return this.traceId;
    }


    public Set<String> getBatches()
    {
        return this.batches;
    }


    public Date getCreatedDate()
    {
        return this.createdDate;
    }


    public String getPoolName()
    {
        return this.poolName;
    }


    public String toString()
    {
        return "BatchesReceivedEvent{traceId='" + this.traceId + "', batches=" + this.batches + ", createdDate=" + this.createdDate + ", poolName=" + this.poolName + "}";
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof BatchesReceivedEvent))
        {
            return false;
        }
        BatchesReceivedEvent other = (BatchesReceivedEvent)o;
        return (new EqualsBuilder())
                        .append(this.traceId, other.traceId)
                        .append(this.batches, other.batches)
                        .append(this.createdDate, other.createdDate)
                        .append(this.poolName, other.poolName)
                        .isEquals();
    }


    public int hashCode()
    {
        return (new HashCodeBuilder())
                        .append(this.traceId)
                        .append(this.batches)
                        .append(this.createdDate)
                        .append(this.poolName)
                        .build().intValue();
    }
}
