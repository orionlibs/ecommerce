package com.hybris.datahub.api.event;

import javax.annotation.concurrent.Immutable;

@Immutable
public abstract class DataHubEvent implements Event
{
    public String getEventName()
    {
        return getClass().getSimpleName();
    }
}
