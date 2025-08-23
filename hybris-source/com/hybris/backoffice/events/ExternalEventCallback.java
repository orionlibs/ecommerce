package com.hybris.backoffice.events;

public interface ExternalEventCallback<T>
{
    void onEvent(T paramT);
}
