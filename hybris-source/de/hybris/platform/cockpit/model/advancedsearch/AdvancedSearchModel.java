package de.hybris.platform.cockpit.model.advancedsearch;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.List;
import java.util.Map;

public interface AdvancedSearchModel
{
    AdvancedSearchParameterContainer getParameterContainer();


    SearchFieldGroup getRootSearchFieldGroup();


    ObjectTemplate getSelectedType();


    List<ObjectTemplate> getTypes();


    List<SearchField> getSearchFields();


    List<SearchField> getVisibleSearchFields();


    List<SearchField> getHiddenSearchFields();


    List<PropertyDescriptor> getSortableProperties();


    PropertyDescriptor getSortedByProperty();


    boolean isSortAscending();


    UIEditor getEditor(SearchField paramSearchField);


    PropertyDescriptor getPropertyDescriptor(SearchField paramSearchField);


    void resetToInitialSearchParameters();


    Map<String, String> getParametersForSearchField(SearchField paramSearchField);
}
