package de.hybris.platform.cockpit.model.advancedsearch;

import de.hybris.platform.cockpit.model.general.UIViewComponent;

public interface UIAdvancedSearchView extends UIViewComponent
{
    void updateSearchField(SearchField paramSearchField, Object paramObject);


    void updateSearchFields();


    void updateSearchGroups();


    void updateTypes();


    void updateSelectedType();


    void updateSortFields();


    void setModel(AdvancedSearchModel paramAdvancedSearchModel);


    AdvancedSearchModel getModel();


    void addAdvancedSearchViewListener(AdvancedSearchViewListener paramAdvancedSearchViewListener);


    void removeAdvancedSearchViewListener(AdvancedSearchViewListener paramAdvancedSearchViewListener);
}
