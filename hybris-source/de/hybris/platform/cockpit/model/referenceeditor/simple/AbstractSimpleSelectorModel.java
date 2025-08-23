package de.hybris.platform.cockpit.model.referenceeditor.simple;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSimpleSelectorModel implements SimpleSelectorModel
{
    protected final List<SimpleSelectorModelListener> listeners = new ArrayList<>();


    public List<SimpleSelectorModelListener> getListeners()
    {
        return this.listeners;
    }


    protected void fireItemChanged()
    {
        for(SimpleSelectorModelListener listener : this.listeners)
        {
            listener.itemChanged();
        }
    }


    protected void fireModeChanged()
    {
        for(SimpleSelectorModelListener listener : this.listeners)
        {
            listener.modeChanged();
        }
    }


    public void addSelectorModelListener(SimpleSelectorModelListener selectorModelListener)
    {
        if(!this.listeners.contains(selectorModelListener))
        {
            this.listeners.add(selectorModelListener);
        }
    }


    public void removeSelectorModelListener(SimpleSelectorModelListener selectorModleListener)
    {
        if(this.listeners.contains(selectorModleListener))
        {
            this.listeners.remove(selectorModleListener);
        }
    }
}
