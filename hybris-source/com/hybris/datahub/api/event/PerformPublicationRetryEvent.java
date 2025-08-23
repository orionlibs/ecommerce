package com.hybris.datahub.api.event;

import java.util.Set;

public class PerformPublicationRetryEvent extends PerformPublicationEvent
{
    public PerformPublicationRetryEvent(long poolId, long actionId, Set<Long> targetPublicationIds, String systemUrl)
    {
        super(poolId, actionId, targetPublicationIds, systemUrl);
    }


    public String toString()
    {
        return "PerformPublicationRetryEvent{actionId=" + getActionId() + ", poolId=" +
                        getPoolId() + ", systemUrl='" +
                        getSystemUrl() + "', targetPublicationIds=" +
                        getTargetPublicationIds() + "}";
    }
}
