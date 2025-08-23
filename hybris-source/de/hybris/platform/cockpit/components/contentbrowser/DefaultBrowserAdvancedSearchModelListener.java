package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchModelListener;
import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.advancedsearch.UIAdvancedSearchView;
import de.hybris.platform.cockpit.session.SearchBrowserModel;

public class DefaultBrowserAdvancedSearchModelListener implements AdvancedSearchModelListener
{
    private final SearchBrowserModel browser;
    private final UIAdvancedSearchView view;


    public DefaultBrowserAdvancedSearchModelListener(SearchBrowserModel browser, UIAdvancedSearchView view)
    {
        this.browser = browser;
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
