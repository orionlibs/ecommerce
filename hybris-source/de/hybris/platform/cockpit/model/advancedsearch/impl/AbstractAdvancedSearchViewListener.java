package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchViewListener;
import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.advancedsearch.SearchFieldGroup;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;

public abstract class AbstractAdvancedSearchViewListener implements AdvancedSearchViewListener
{
    protected final DefaultAdvancedSearchModel model;


    public AbstractAdvancedSearchViewListener(DefaultAdvancedSearchModel model)
    {
        if(model == null)
        {
            throw new IllegalArgumentException("Model can not be null.");
        }
        this.model = model;
    }


    public void addSortFieldClicked(SearchField field)
    {
        this.model.addSortableProperty(this.model.getPropertyDescriptor(field));
    }


    public void hideSearchFieldClicked(SearchField field)
    {
        this.model.hideSearchField(field);
    }


    public void removeSortFieldClicked(SearchField field)
    {
        this.model.removeSortableProperty(this.model.getPropertyDescriptor(field));
    }


    public void selectedTypeComboItemChanged(ObjectTemplate type)
    {
        this.model.setSelectedType(type);
    }


    public void showSearchFieldClicked(SearchField field)
    {
        this.model.showSearchField(field);
    }


    public void hideSearchFieldGroupClicked(SearchFieldGroup group)
    {
        this.model.hideSearchFieldGroup(group);
    }


    public void showSearchFieldGroupClicked(SearchFieldGroup group)
    {
        this.model.showSearchFieldGroup(group);
    }
}
