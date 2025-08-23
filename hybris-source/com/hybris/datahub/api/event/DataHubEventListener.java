package com.hybris.datahub.api.event;

public interface DataHubEventListener<T extends Event>
{
    void handleEvent(T paramT);


    Class<T> getEventClass();


    boolean executeInTransaction();
}
