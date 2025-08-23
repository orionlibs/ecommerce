package com.hybris.datahub.service;

import com.hybris.datahub.api.event.DataHubEventListener;
import com.hybris.datahub.api.event.Event;
import java.util.List;

public interface DataHubEventListenerRegistry
{
    void notifyListeners(Event paramEvent);


    <T extends Event> List<DataHubEventListener<T>> getListenersRegisteredForEvent(Class<T> paramClass);


    void addListener(DataHubEventListener<?> paramDataHubEventListener);
}
