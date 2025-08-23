package com.hybris.datahub.api.event;

import java.io.Serializable;
import javax.annotation.concurrent.Immutable;

@Immutable
public class DataHubInitializationCompletedEvent extends DataHubEvent implements Serializable
{
    public int hashCode()
    {
        return 0;
    }


    public boolean equals(Object obj)
    {
        return (obj != null && DataHubInitializationCompletedEvent.class.equals(obj.getClass()));
    }


    public String toString()
    {
        return "DataHubInitializationCompletedEvent{}";
    }
}
