package de.hybris.platform.cockpit.model.referenceeditor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSelectorModel implements SelectorModel
{
    protected final List<SelectorModelListener> listeners = new ArrayList<>();


    public List<SelectorModelListener> getListeners()
    {
        return this.listeners;
    }


    protected void fireItemsChanged()
    {
        for(SelectorModelListener listener : this.listeners)
        {
            listener.itemsChanged();
        }
    }


    protected void fireModeChanged()
    {
        for(SelectorModelListener listener : this.listeners)
        {
            listener.modeChanged();
        }
    }


    protected void fireSelectionModeChanged()
    {
        for(SelectorModelListener listener : this.listeners)
        {
            listener.selectionModeChanged();
        }
    }


    protected void fireChanged()
    {
        for(SelectorModelListener listener : this.listeners)
        {
            listener.changed();
        }
    }


    public void addSelectorModelListener(SelectorModelListener selectorModelListener)
    {
        if(!this.listeners.contains(selectorModelListener))
        {
            this.listeners.add(selectorModelListener);
        }
    }


    public void removeSelectorModelListener(SelectorModelListener selectorModleListener)
    {
        if(this.listeners.contains(selectorModleListener))
        {
            this.listeners.remove(selectorModleListener);
        }
    }
}
