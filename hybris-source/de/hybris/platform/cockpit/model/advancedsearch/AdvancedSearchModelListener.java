package de.hybris.platform.cockpit.model.advancedsearch;

public interface AdvancedSearchModelListener
{
    void changed();


    void selectedTypeChanged();


    void typesChanged();


    void searchFieldVisibilityChanged();


    void searchFieldChanged(SearchField paramSearchField);


    void searchFieldGroupVisibilityChanged();


    void sortSearchFieldsChanged();
}
