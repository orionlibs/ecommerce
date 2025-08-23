package com.hybris.backoffice.events;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import java.util.Collection;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBackofficeEventListener<T extends AbstractEvent> extends AbstractEventListener<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBackofficeEventListener.class);
    private final Collection<ExternalEventCallback<T>> callbacks = new HashSet<>();


    public void registerCallback(ExternalEventCallback<T> callback)
    {
        if(callback != null)
        {
            this.callbacks.add(callback);
        }
        else
        {
            LOGGER.warn("Skipping registration, because callback is null.", new IllegalArgumentException("callback must not be null"));
        }
    }


    public void unregisterCallback(ExternalEventCallback<T> callback)
    {
        this.callbacks.remove(callback);
    }


    public boolean isCallbackRegistered(ExternalEventCallback<T> callback)
    {
        return this.callbacks.contains(callback);
    }


    protected void onEvent(T event)
    {
        for(ExternalEventCallback<T> callback : this.callbacks)
        {
            callback.onEvent(event);
        }
    }
}
