package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchViewListener;
import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.advancedsearch.SearchFieldGroup;
import de.hybris.platform.cockpit.model.advancedsearch.UIAdvancedSearchView;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.zkoss.zul.Div;

public abstract class AbstractAdvancedSearchView extends Div implements UIAdvancedSearchView
{
    private final List<AdvancedSearchViewListener> listeners = new ArrayList<>();
    private boolean editMode = false;


    public void addAdvancedSearchViewListener(AdvancedSearchViewListener listener)
    {
        this.listeners.add(listener);
    }


    public void removeAdvancedSearchViewListener(AdvancedSearchViewListener listener)
    {
        this.listeners.remove(listener);
    }


    public abstract AdvancedSearchParameterContainer getParameterContainer();


    protected void fireChangeSelectedType(ObjectTemplate type)
    {
        List<AdvancedSearchViewListener> avListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchViewListener listener : avListeners)
        {
            listener.selectedTypeComboItemChanged(type);
        }
    }


    protected void fireHideSearchField(SearchField field)
    {
        List<AdvancedSearchViewListener> avListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchViewListener listener : avListeners)
        {
            listener.hideSearchFieldClicked(field);
        }
    }


    protected void fireShowSearchField(SearchField field)
    {
        List<AdvancedSearchViewListener> avListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchViewListener listener : avListeners)
        {
            listener.showSearchFieldClicked(field);
        }
    }


    protected void fireAddSortField(SearchField field)
    {
        List<AdvancedSearchViewListener> avListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchViewListener listener : avListeners)
        {
            listener.addSortFieldClicked(field);
        }
    }


    protected void fireRemoveSortField(SearchField field)
    {
        List<AdvancedSearchViewListener> avListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchViewListener listener : avListeners)
        {
            listener.removeSortFieldClicked(field);
        }
    }


    protected void fireHideSearchFieldGroup(SearchFieldGroup group)
    {
        List<AdvancedSearchViewListener> avListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchViewListener listener : avListeners)
        {
            listener.hideSearchFieldGroupClicked(group);
        }
    }


    protected void fireShowSearchFieldGroup(SearchFieldGroup group)
    {
        List<AdvancedSearchViewListener> avListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchViewListener listener : avListeners)
        {
            listener.showSearchFieldGroupClicked(group);
        }
    }


    public void fireSearch()
    {
        AdvancedSearchParameterContainer parameterContainer = getParameterContainer();
        List<AdvancedSearchViewListener> avListeners = new LinkedList<>(this.listeners);
        for(AdvancedSearchViewListener listener : avListeners)
        {
            listener.searchButtonClicked(parameterContainer);
        }
    }


    public boolean isInEditMode()
    {
        return this.editMode;
    }


    public void setEditMode(boolean value)
    {
        this.editMode = value;
    }
}
