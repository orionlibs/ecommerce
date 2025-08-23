/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.viewmodel;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

public class ExampleWidgetViewModel
{
    @WireVariable
    private WidgetInstanceManager widgetInstanceManager;


    public TypedSettingsMap getWidgetSettings()
    {
        return widgetInstanceManager.getWidgetSettings();
    }


    public int getTextHashcode()
    {
        final String text = this.getText();
        return text == null ? 0 : text.hashCode();
    }


    public String getText()
    {
        return getWidgetModel().getValue("mytext", String.class);
    }


    @NotifyChange({"textHashcode", "text"})
    public void setText(final String text)
    {
        getWidgetModel().setValue("mytext", text);
    }


    @Command
    @NotifyChange({"textHashcode", "text"})
    public void bla()
    {
        setText("exampleText");
        widgetInstanceManager.sendOutput("textOutput", getText());
    }


    @Command("sendOutput")
    public void sendSocketOutput()
    {
        widgetInstanceManager.sendOutput("textOutput", getText());
    }


    public WidgetModel getWidgetModel()
    {
        return widgetInstanceManager.getModel();
    }


    @SocketEvent
    @NotifyChange({"textHashcode", "text"})
    public void textInput(final String data)
    {
        setText(data);
    }


    @GlobalCockpitEvent(eventName = "sample_event", scope = CockpitEvent.APPLICATION)
    @NotifyChange({"textHashcode", "text"})
    public void handleGlobalEvent(final CockpitEvent event)
    {
        Messagebox.show("global event " + event);
        setText(String.valueOf(event.getData()));
    }
}
