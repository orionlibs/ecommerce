/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.annotations.ViewEvents;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

/**
 *
 */
public class ExampleDefaultWidgetController extends DefaultWidgetController
{
    private static final long serialVersionUID = 7795325168626716276L;
    private static final Logger LOG = LoggerFactory.getLogger(ExampleDefaultWidgetController.class);
    private static final String MY_TEXT = "mytext";
    // Components
    protected transient Label myLabel; // Autowired by name convention, variable name must
    // be same as id in zul
    protected transient Textbox myTextbox; // Autowired
    // variable name must be same as
    // spring bean id
    private transient CockpitEventQueue cockpitEventQueue;
    private transient Editor dateEditor;


    @ViewEvent(componentID = "dateEditor", eventName = Editor.ON_VALUE_CHANGED)
    public void onEditorValueModified()
    {
        final Date date = (Date)dateEditor.getValue();
        final Date obj = getModel().getValue("dateEditorProperty", Date.class);
        if(LOG.isWarnEnabled())
        {
            LOG.warn(date == null ? null : date.toString());
            LOG.warn(obj == null ? null : obj.toString());
        }
    }


    @Override
    public void initialize(final Component comp)
    {
        setWidgetTitle(getLabel("title"));
    }


    @SocketEvent(socketId = "textInput")
    public void handleInput(final String text)
    {
        /*
         * Stores the value to the widget model which has a scope of session. It calls getModel().setAttribute("mytext",
         * text); but in addition notifies any object interested on widget model changes.
         */
        setValue(MY_TEXT, text);
        setValue("myHashText", getTextHashCode());
        myLabel.setValue(String.valueOf(getTextHashCode()));
        myTextbox.setText(text);
    }


    @ViewEvents(
                    {@ViewEvent(componentID = "myTextbox", eventName = Events.ON_CHANGE),
                                    @ViewEvent(componentID = "myBlaButton", eventName = Events.ON_CLICK)})
    public void handleTextChange(final Event event)
    {
        String text = null;
        if(Events.ON_CLICK.equals(event.getName()))
        {
            text = "exampleText";
        }
        else
        {
            text = myTextbox.getText();
        }
        handleInput(text);
    }


    @ViewEvent(componentID = "myOutputButton", eventName = Events.ON_CLICK)
    public void sendTextOutput()
    {
        sendOutput("textOutput", getModel().getValue(MY_TEXT, String.class));
    }


    private int getTextHashCode()
    {
        final Object text = getModel().getValue(MY_TEXT, String.class);
        return text == null ? 0 : text.hashCode();
    }


    @ViewEvent(componentID = "btn1", eventName = Events.ON_CLICK)
    public void publishGlobalEvent1()
    {
        cockpitEventQueue.publishEvent(new DefaultCockpitEvent("exampleEvent1", "some data", null));
    }


    @ViewEvent(componentID = "btn2", eventName = Events.ON_CLICK)
    public void publishGlobalEvent2()
    {
        cockpitEventQueue.publishEvent(new DefaultCockpitEvent("exampleEvent2", "some other data", null));
    }


    @ViewEvent(componentID = "btn3", eventName = Events.ON_CLICK)
    public void publishGlobalEvent3()
    {
        cockpitEventQueue.publishEvent(new DefaultCockpitEvent("exampleEvent3", "some other data", null));
    }


    @GlobalCockpitEvent(eventName = "exampleEvent1", scope = CockpitEvent.DESKTOP)
    public void handleGlobalEvent1(final CockpitEvent event)
    {
        Messagebox.show(String.valueOf(event.getData()));
    }


    @GlobalCockpitEvent(eventName = "exampleEvent2", scope = CockpitEvent.SESSION)
    public void handleGlobalEvent2(final CockpitEvent event)
    {
        Messagebox.show(String.valueOf(event.getData()));
    }


    @GlobalCockpitEvent(eventName = "exampleEvent3", scope = CockpitEvent.APPLICATION)
    public void handleGlobalEvent3(final CockpitEvent event)
    {
        Messagebox.show(String.valueOf(event.getData()));
    }


    public Label getMyLabel()
    {
        return myLabel;
    }


    public Textbox getMyTextbox()
    {
        return myTextbox;
    }


    public CockpitEventQueue getCockpitEventQueue()
    {
        return cockpitEventQueue;
    }


    public Editor getDateEditor()
    {
        return dateEditor;
    }
}
