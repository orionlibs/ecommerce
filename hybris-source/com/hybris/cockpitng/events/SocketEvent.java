/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.events;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 *
 */
public class SocketEvent extends Event
{
    private static final long serialVersionUID = -7725837893428446122L;
    private final String sourceWidgetID;
    private final String sourceSocketID;


    public SocketEvent(final String name, final Component target, final Object data, final String sourceWidgetID,
                    final String sourceSocketID)
    {
        super(name, target, data);
        this.sourceSocketID = sourceSocketID;
        this.sourceWidgetID = sourceWidgetID;
    }


    public String getSourceWidgetID()
    {
        return sourceWidgetID;
    }


    public String getSourceSocketID()
    {
        return sourceSocketID;
    }
}
