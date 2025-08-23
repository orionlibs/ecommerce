package com.hybris.datahub.api.event;

import javax.annotation.concurrent.Immutable;

@Immutable
public class InitiateCompositionEvent extends CompositionEvent
{
    private static final long serialVersionUID = 1971517044950412740L;


    public InitiateCompositionEvent(long poolId)
    {
        super(poolId);
    }


    public String toString()
    {
        return "InitiateCompositionEvent{poolId=" + getPoolId() + "}";
    }
}
