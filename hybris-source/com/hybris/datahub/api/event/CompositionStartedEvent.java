package com.hybris.datahub.api.event;

import javax.annotation.concurrent.Immutable;

@Immutable
public class CompositionStartedEvent extends CompositionProcessEvent implements ProcessStartedEvent
{
    private static final long serialVersionUID = -4126545587358088727L;


    public CompositionStartedEvent(long poolId, long actionId)
    {
        super(poolId, actionId);
    }


    public String toString()
    {
        return "CompositionStartedEvent{actionId=" + getActionId() + ", poolId=" +
                        getPoolId() + "}";
    }
}
