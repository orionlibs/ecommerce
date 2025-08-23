package com.hybris.datahub.api.event;

import javax.annotation.concurrent.Immutable;

@Immutable
public class PublicationStartedEvent extends PublicationProcessEvent implements ProcessStartedEvent
{
    private static final long serialVersionUID = 2043088366238640361L;


    public PublicationStartedEvent(long poolId, long actionId)
    {
        super(poolId, actionId);
    }


    public String toString()
    {
        return "PublicationStartedEvent{actionId=" + getActionId() + ", poolId=" +
                        getPoolId() + "}";
    }
}
