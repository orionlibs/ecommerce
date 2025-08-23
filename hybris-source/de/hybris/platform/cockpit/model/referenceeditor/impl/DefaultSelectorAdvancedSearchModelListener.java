package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModelListener;
import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.advancedsearch.UIAdvancedSearchView;

public class DefaultSelectorAdvancedSearchModelListener implements AdvancedSearchModelListener
{
    private final UIAdvancedSearchView view;


    public DefaultSelectorAdvancedSearchModelListener(UIAdvancedSearchView view)
    {
        this.view = view;
    }


    public void changed()
    {
        this.view.update();
    }


    public void searchFieldChanged(SearchField searchField)
    {
        this.view.updateSearchField(searchField, null);
    }


    public void searchFieldGroupVisibilityChanged()
    {
        this.view.updateSearchGroups();
    }


    public void searchFieldVisibilityChanged()
    {
        this.view.updateSearchFields();
    }


    public void selectedTypeChanged()
    {
        this.view.update();
    }


    public void sortSearchFieldsChanged()
    {
        this.view.updateSortFields();
    }


    public void typesChanged()
    {
        this.view.updateTypes();
    }
}
