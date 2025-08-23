package com.hybris.backoffice.jalo;

public interface PersistenceLayerSessionListener
{
    void sessionCreated(Object paramObject);


    void sessionClosed(Object paramObject);
}
