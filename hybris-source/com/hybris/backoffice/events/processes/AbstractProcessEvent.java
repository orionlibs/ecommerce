package com.hybris.backoffice.events.processes;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public class AbstractProcessEvent<T extends AbstractEvent> extends AbstractEvent implements ClusterAwareEvent
{
    private final T processEvent;


    public AbstractProcessEvent(T processEvent)
    {
        this.processEvent = processEvent;
    }


    public T getProcessEvent()
    {
        return this.processEvent;
    }


    public boolean publish(int sourceNodeId, int targetNodeId)
    {
        return true;
    }
}
