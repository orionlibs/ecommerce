/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.simplecast;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;

public class SimpleCastController extends DefaultWidgetController
{
    protected static final String SOCKET_IN_INPUT = "input";
    protected static final String SOCKET_OUT_OUTPUT = "output";
    private static final long serialVersionUID = -2255863298958387170L;


    @SocketEvent(socketId = SOCKET_IN_INPUT)
    public void input(final Object input)
    {
        this.sendOutput(SOCKET_OUT_OUTPUT, input);
    }
}
