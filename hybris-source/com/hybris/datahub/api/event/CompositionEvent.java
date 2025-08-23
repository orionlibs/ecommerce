package com.hybris.datahub.api.event;

public abstract class CompositionEvent extends PoolEvent
{
    private static final long serialVersionUID = -7255984234090418876L;


    public CompositionEvent(long poolId)
    {
        super(poolId);
    }
}
