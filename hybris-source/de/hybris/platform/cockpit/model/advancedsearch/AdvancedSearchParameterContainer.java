package de.hybris.platform.cockpit.model.advancedsearch;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.Map;

public interface AdvancedSearchParameterContainer
{
    boolean hasValues();


    boolean hasValues(boolean paramBoolean);


    void clear();


    void put(SearchField paramSearchField, ConditionValueContainer paramConditionValueContainer);


    ConditionValueContainer remove(SearchField paramSearchField);


    ConditionValueContainer get(SearchField paramSearchField);


    Map<SearchField, ConditionValueContainer> getSearchFieldValueMap();


    void setSortProperty(PropertyDescriptor paramPropertyDescriptor);


    PropertyDescriptor getSortProperty();


    void setSelectedType(ObjectTemplate paramObjectTemplate);


    ObjectTemplate getSelectedType();


    void setSortAscending(boolean paramBoolean);


    boolean isSortAscending();


    boolean isExcludeSubtypes();


    void setExcludeSubtypes(boolean paramBoolean);
}
