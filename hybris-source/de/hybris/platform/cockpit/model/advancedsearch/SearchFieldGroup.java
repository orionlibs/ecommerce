package de.hybris.platform.cockpit.model.advancedsearch;

import java.util.List;

public interface SearchFieldGroup
{
    String getLabel();


    boolean isVisible();


    SearchFieldGroup getParentSearchFieldGroup();


    List<SearchFieldGroup> getSearchFieldGroups();


    List<SearchFieldGroup> getAllSearchFieldGroups();


    List<SearchFieldGroup> getVisibleSearchFieldGroups();


    List<SearchFieldGroup> getAllVisibleSearchFieldGroups();


    List<SearchFieldGroup> getHiddenSearchFieldGroups();


    List<SearchFieldGroup> getAllHiddenSearchFieldGroups();


    List<SearchFieldGroup> getAllPartiallyVisibleGroups();


    List<SearchFieldGroup> getPartiallyVisibleGroups();


    List<SearchField> getSearchFields();


    List<SearchField> getAllSearchFields();


    List<SearchField> getVisibleSearchFields();


    List<SearchField> getAllVisibleSearchFields();


    List<SearchField> getHiddenSearchFields();


    List<SearchField> getAllHiddenSearchFields();
}
