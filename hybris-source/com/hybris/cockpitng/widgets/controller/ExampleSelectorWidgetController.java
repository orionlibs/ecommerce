/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.SelectorWidgetController;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

/**
 *
 */
public class ExampleSelectorWidgetController extends SelectorWidgetController<Component>
{
    private static final long serialVersionUID = 9045089858798825344L;
    // Components
    @Wire
    protected transient Label myLabel; // Autowired by name convention, variable name must be same as id in zul
    @Wire("#myTextbox")
    protected transient Textbox tbox; // Explicitly wired


    @SocketEvent(socketId = "textInput")
    public void handleInput(final String text)
    {
        getModel().setValue("mytext", text);
        myLabel.setValue(String.valueOf(text.hashCode()));
        tbox.setText(text);
    }


    @Listen("onChange = #myTextbox; onClick = #myBlaButton")
    public void handleTextChange(final Event event)
    {
        String text = null;
        if(Events.ON_CLICK.equals(event.getName()))
        {
            text = "exampleText";
        }
        else
        {
            text = tbox.getText();
        }
        handleInput(text);
    }


    @Listen("onClick = #myOutputButton")
    public void sendTextOutput()
    {
        sendOutput("textOutput", getModel().getValue("mytext", String.class));
    }
}
