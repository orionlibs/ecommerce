/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

/**
 * Example widget demonstrating data passing between widgets.
 */
public class DataPassingWidgetController extends DefaultWidgetController
{
    protected static final String SEND_COMPONENT = "send";
    protected static final String SOCKET_IN_INPUT_DATA = "inputData";
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(DataPassingWidgetController.class);
    private Label dataReceiver;
    private Textbox sendData;


    @ViewEvent(componentID = SEND_COMPONENT, eventName = Events.ON_CLICK)
    public void sendOutputData()
    {
        sendOutput("outputData", sendData.getText());
        LOG.info("Data received: " + sendData.getText());
    }


    @SocketEvent(socketId = SOCKET_IN_INPUT_DATA)
    public void updateReceiver(final String data)
    {
        dataReceiver.setValue(data);
        LOG.info("Data sent: {}", data);
    }


    public Textbox getSendData()
    {
        return sendData;
    }


    public Label getDataReceiver()
    {
        return dataReceiver;
    }
}
