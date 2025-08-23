package com.hybris.datahub.api.event;

import javax.annotation.concurrent.Immutable;

@Immutable
public class PublicationCompletedEvent extends PublicationProcessEvent implements ProcessCompletedEvent
{
    private static final long serialVersionUID = -1203292541021654277L;


    public PublicationCompletedEvent(long poolId, long actionId)
    {
        super(poolId, actionId);
    }


    public String toString()
    {
        return "PublicationCompletedEvent{actionId=" + getActionId() + ", poolId=" +
                        getPoolId() + "}";
    }
}
