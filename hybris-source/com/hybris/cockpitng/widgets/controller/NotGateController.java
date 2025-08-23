/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;

/**
 * A simple NOT gate controller.
 */
public class NotGateController extends DefaultWidgetController
{
    public static final String SOCKET_OUT_OUTPUT = "output";
    public static final String SOCKET_IN_INPUT = "input";
    private static final long serialVersionUID = -7999433060741724298L;


    /**
     * Sends a boolean output that is a boolean complement of the input.
     *
     * @param bool
     */
    @SocketEvent(socketId = SOCKET_IN_INPUT)
    public void input(final Boolean bool)
    {
        if(Boolean.TRUE.equals(bool))
        {
            sendOutput(SOCKET_OUT_OUTPUT, Boolean.FALSE);
        }
        else
        {
            sendOutput(SOCKET_OUT_OUTPUT, Boolean.TRUE);
        }
    }
}
