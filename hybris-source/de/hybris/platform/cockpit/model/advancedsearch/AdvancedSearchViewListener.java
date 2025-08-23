package de.hybris.platform.cockpit.model.advancedsearch;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;

public interface AdvancedSearchViewListener
{
    void selectedTypeComboItemChanged(ObjectTemplate paramObjectTemplate);


    void hideSearchFieldClicked(SearchField paramSearchField);


    void showSearchFieldClicked(SearchField paramSearchField);


    void addSortFieldClicked(SearchField paramSearchField);


    void removeSortFieldClicked(SearchField paramSearchField);


    void searchButtonClicked(AdvancedSearchParameterContainer paramAdvancedSearchParameterContainer);


    void showSearchFieldGroupClicked(SearchFieldGroup paramSearchFieldGroup);


    void hideSearchFieldGroupClicked(SearchFieldGroup paramSearchFieldGroup);
}
