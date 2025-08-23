package com.hybris.backoffice.events.sync;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

@Deprecated(since = "6.6", forRemoval = true)
public class AbstractSyncEvent<T extends AbstractEvent> extends AbstractEvent implements ClusterAwareEvent
{
    private final T syncEvent;


    public AbstractSyncEvent(T syncEvent)
    {
        this.syncEvent = syncEvent;
    }


    public T getSyncEvent()
    {
        return this.syncEvent;
    }


    public boolean publish(int sourceNodeId, int targetNodeId)
    {
        return true;
    }
}
