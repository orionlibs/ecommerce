package com.hybris.datahub.api.event;

import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ResetPublicationEvent extends PublicationProcessEvent
{
    private final String systemUrl;
    private final Set<Long> targetPublicationIds;


    public ResetPublicationEvent(long poolId, long actionId, Set<Long> targetPublicationIds, String systemUrl)
    {
        super(poolId, actionId);
        this.targetPublicationIds = targetPublicationIds;
        this.systemUrl = systemUrl;
    }


    public Set<Long> getTargetPublicationIds()
    {
        return this.targetPublicationIds;
    }


    public String getSystemUrl()
    {
        return this.systemUrl;
    }


    public String toString()
    {
        return "ResetPublicationEvent{poolId=" + getPoolId() + ", actionId=" +
                        getActionId() + ", eventName=" +
                        getEventName() + ", processId=" +
                        getProcessId() + ", systemUrl=" + this.systemUrl + ", targetPublicationIds=" + this.targetPublicationIds + "}";
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
        ResetPublicationEvent that = (ResetPublicationEvent)o;
        return (new EqualsBuilder())
                        .appendSuper(super.equals(o))
                        .append(this.systemUrl, that.systemUrl)
                        .append(this.targetPublicationIds, that.targetPublicationIds)
                        .isEquals();
    }


    public int hashCode()
    {
        return (new HashCodeBuilder(17, 37))
                        .appendSuper(super.hashCode())
                        .append(this.systemUrl)
                        .append(this.targetPublicationIds)
                        .toHashCode();
    }
}
