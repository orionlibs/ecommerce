/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

/**
 * Chat controller.
 */
public class ChatWidgetController extends DefaultWidgetController
{
    protected static final String SOCKET_IN_MESSAGE_IN = "inMessage";
    protected static final String SEND_BUTTON = "sendButton";
    protected static final String CLEAR_BUTTON = "clearButton";
    private static final long serialVersionUID = -7270756539112912344L;
    @Wire
    private Textbox outgoingMessageTextbox;
    @Wire
    private Div conversationView;


    /**
     * send method.
     */
    @ViewEvent(componentID = SEND_BUTTON, eventName = Events.ON_CLICK)
    public void send()
    {
        sendOutput("outMessage", outgoingMessageTextbox.getText());
        updateConversationWindow(outgoingMessageTextbox.getText(), MessageDirection.OUT);
        clearOutgoingMessage();
    }


    /**
     * receive method.
     */
    @SocketEvent(socketId = SOCKET_IN_MESSAGE_IN)
    public void receive(final String msg)
    {
        updateConversationWindow(msg, MessageDirection.IN);
    }


    /**
     * updateConvesationWindow mehtod.
     */
    private void updateConversationWindow(final String msg, final MessageDirection msgDir)
    {
        final String userHeaderText;
        switch(msgDir)
        {
            case IN:
                userHeaderText = getLabel("chat.conversation.someonesaid");
                break;
            case OUT:
                userHeaderText = getLabel("chat.conversation.mesaid");
                break;
            default:
                userHeaderText = "it should never reach the statement";
        }
        final Div dirDiv = new Div();
        final Label userHeader = new Label(userHeaderText);
        userHeader.setParent(dirDiv);
        userHeader.setClass("userHeader");
        dirDiv.setParent(conversationView);
        final Div msgDiv = new Div();
        final Label msgContent = new Label(msg);
        msgContent.setParent(msgDiv);
        msgContent.setClass("messageContent");
        msgDiv.setParent(conversationView);
    }


    /**
     * clearOutgoingMessage method.
     */
    @ViewEvent(componentID = CLEAR_BUTTON, eventName = Events.ON_CLICK)
    public void clearOutgoingMessage()
    {
        outgoingMessageTextbox.setText(StringUtils.EMPTY);
    }


    public Div getConversationView()
    {
        return conversationView;
    }


    public Textbox getOutgoingMessageTextbox()
    {
        return outgoingMessageTextbox;
    }
}
