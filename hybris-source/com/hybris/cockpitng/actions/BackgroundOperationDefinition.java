/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

import com.hybris.cockpitng.core.async.Operation;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 *
 */
public class BackgroundOperationDefinition
{
    private String id;
    private Operation operation;
    private EventListener<Event> callbackEvent;
    private String busyMessage;


    public String getId()
    {
        return id;
    }


    /**
     * @param id the id to set
     */
    public void setId(final String id)
    {
        this.id = id;
    }


    /**
     * @return the operation
     */
    public Operation getOperation()
    {
        return operation;
    }


    /**
     * @param operation the operation to set
     */
    public void setOperation(final Operation operation)
    {
        this.operation = operation;
    }


    /**
     * @return the callbackEvent
     */
    public EventListener<Event> getCallbackEvent()
    {
        return callbackEvent;
    }


    /**
     * @param callbackEvent the callbackEvent to set
     */
    public void setCallbackEvent(final EventListener<Event> callbackEvent)
    {
        this.callbackEvent = callbackEvent;
    }


    /**
     * @return the busyMessage
     */
    public String getBusyMessage()
    {
        return busyMessage;
    }


    /**
     * @param busyMessage the busyMessage to set
     */
    public void setBusyMessage(final String busyMessage)
    {
        this.busyMessage = busyMessage;
    }
}
