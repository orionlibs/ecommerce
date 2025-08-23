package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModel;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModelListener;
import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractAdvancedSearchModel implements AdvancedSearchModel
{
    private final List<AdvancedSearchModelListener> listeners = new ArrayList<>();


    protected void fireChanged()
    {
        List<AdvancedSearchModelListener> asListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchModelListener listener : asListeners)
        {
            listener.changed();
        }
    }


    protected void fireSelectedTypeChanged()
    {
        List<AdvancedSearchModelListener> asListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchModelListener listener : asListeners)
        {
            listener.selectedTypeChanged();
        }
    }


    protected void fireTypesChanged()
    {
        List<AdvancedSearchModelListener> asListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchModelListener listener : asListeners)
        {
            listener.typesChanged();
        }
    }


    protected void fireSearchFieldVisibilityChanged()
    {
        List<AdvancedSearchModelListener> asListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchModelListener listener : asListeners)
        {
            listener.searchFieldVisibilityChanged();
        }
    }


    protected void fireSearchFieldChanged(SearchField searchField)
    {
        List<AdvancedSearchModelListener> asListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchModelListener listener : asListeners)
        {
            listener.searchFieldChanged(searchField);
        }
    }


    protected void fireSearchFieldGroupVisibilityChanged()
    {
        List<AdvancedSearchModelListener> asListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchModelListener listener : asListeners)
        {
            listener.searchFieldGroupVisibilityChanged();
        }
    }


    protected void fireSortSearchFieldsChanged()
    {
        List<AdvancedSearchModelListener> asListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchModelListener listener : asListeners)
        {
            listener.sortSearchFieldsChanged();
        }
    }


    public void addAdvancedSearchModelListener(AdvancedSearchModelListener listener)
    {
        if(!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }


    public void removeAdvancedSearchModelListener(AdvancedSearchModelListener listener)
    {
        this.listeners.remove(listener);
    }
}
