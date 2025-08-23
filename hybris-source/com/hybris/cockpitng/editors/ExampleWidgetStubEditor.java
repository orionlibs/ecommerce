/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors;

import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.events.SocketEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

/**
 *  ExampleWidgetStubEditor class.
 */
public class ExampleWidgetStubEditor extends AbstractComponentWidgetAdapterAware implements CockpitEditorRenderer<Object>
{
    @Override
    public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        final Div cnt = new Div();
        final Button button = new Button("Click me");
        final Label label = new Label("[no value]");
        button.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {
            public void onEvent(final Event event)
            {
                sendOutput("testOutput", context);
            }
        });
        addSocketInputEventListener("testInput", new EventListener<SocketEvent>()
        {
            @Override
            public void onEvent(final SocketEvent event)
            {
                label.setValue("Got " + event.getData() + " from widget " + event.getSourceWidgetID());
            }
        });
        parent.appendChild(cnt);
        cnt.appendChild(button);
        cnt.appendChild(label);
    }
}
