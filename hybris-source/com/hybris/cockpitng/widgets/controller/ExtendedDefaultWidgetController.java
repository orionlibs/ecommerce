/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;

public class ExtendedDefaultWidgetController extends ExampleDefaultWidgetController
{
    private static final long serialVersionUID = 1L;


    @ViewEvent(componentID = "additionalButton", eventName = Events.ON_CLICK)
    public void additionalButtonClicked()
    {
        Messagebox.show("Extended functionality from " + this.getClass().getName());
    }


    @Override
    @SocketEvent(socketId = "textInput")
    public void handleInput(final String text)
    {
        Messagebox.show("Custom stuff before input is passed to super controller " + this.getClass().getSuperclass().getName());
        super.handleInput(text);
    }
}
