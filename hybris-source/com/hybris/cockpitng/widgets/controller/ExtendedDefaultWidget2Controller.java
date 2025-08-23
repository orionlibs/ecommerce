/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;

public class ExtendedDefaultWidget2Controller extends ExampleDefaultWidgetController
{
    private static final long serialVersionUID = 1L;
    protected transient Component myBlaButton = null;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        final Button btn = new Button("Additional Button");
        btn.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {
            public void onEvent(final Event event) throws Exception
            {
                Messagebox.show("This additional button has been added by sub widget controller");
            }
        });
        if(myBlaButton != null)
        {
            myBlaButton.getParent().insertBefore(btn, myBlaButton);
        }
    }
}
