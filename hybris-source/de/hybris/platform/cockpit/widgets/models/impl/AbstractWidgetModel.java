package de.hybris.platform.cockpit.widgets.models.impl;

import de.hybris.platform.cockpit.widgets.events.WidgetModelEvent;
import de.hybris.platform.cockpit.widgets.events.WidgetModelListener;
import de.hybris.platform.cockpit.widgets.models.WidgetModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractWidgetModel implements WidgetModel
{
    private final List<WidgetModelListener> listeners = new ArrayList<>();


    public void addWidgetModelListener(WidgetModelListener listener)
    {
        if(!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }


    public void removeWidgetModelLlistener(WidgetModelListener listener)
    {
        this.listeners.remove(listener);
    }


    protected void notifyListeners(WidgetModelEvent event)
    {
        for(WidgetModelListener listener : this.listeners)
        {
            listener.onModelEvent(event);
        }
    }


    public void notifyListeners()
    {
        WidgetModelEvent event = new WidgetModelEvent(this);
        notifyListeners(event);
    }


    protected List<WidgetModelListener> getWidgetModelListeners()
    {
        return Collections.unmodifiableList(this.listeners);
    }
}
