package com.hybris.datahub.api.event;

public abstract class PublicationEvent extends PoolEvent
{
    private static final long serialVersionUID = -5673696254800508889L;


    public PublicationEvent(long poolId)
    {
        super(poolId);
    }
}
