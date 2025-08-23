package de.hybris.platform.platformbackoffice.editors;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

@Deprecated(since = "6.6", forRemoval = true)
public abstract class AbstractDecoratedEditorListener<C extends AbstractDecoratedEditorController> implements EventListener<Event>
{
    protected C controller;


    public void registerAsListenerFor(String eventName, AbstractComponent component)
    {
        component.addEventListener(eventName, this);
    }


    protected void reload()
    {
        this.controller.getAncestorEditor().getWidgetInstanceManager().getModel().changed();
        this.controller.getAncestorEditor().getWidgetInstanceManager().getModel()
                        .setValue("valueChanged", Boolean.FALSE);
        this.controller.getAncestorEditor().reload();
    }
}
