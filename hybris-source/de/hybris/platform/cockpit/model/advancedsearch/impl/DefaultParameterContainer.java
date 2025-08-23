package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.advancedsearch.AdvancedSearchParameterContainer;
import de.hybris.platform.cockpit.model.advancedsearch.ConditionValueContainer;
import de.hybris.platform.cockpit.model.advancedsearch.SearchField;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultParameterContainer implements AdvancedSearchParameterContainer
{
    private final Map<SearchField, ConditionValueContainer> fieldValueMap = new HashMap<>();
    private PropertyDescriptor sortProperty = null;
    private boolean sortAsc = true;
    private boolean excludeSubtypes = false;
    private ObjectTemplate type = null;


    public Map<SearchField, ConditionValueContainer> getSearchFieldValueMap()
    {
        return Collections.unmodifiableMap(this.fieldValueMap);
    }


    public PropertyDescriptor getSortProperty()
    {
        return this.sortProperty;
    }


    public void setSortProperty(PropertyDescriptor sortProp)
    {
        this.sortProperty = sortProp;
    }


    public void setSortAscending(boolean asc)
    {
        this.sortAsc = asc;
    }


    public boolean isSortAscending()
    {
        return this.sortAsc;
    }


    public void clear()
    {
        this.fieldValueMap.clear();
    }


    public ConditionValueContainer get(SearchField field)
    {
        return this.fieldValueMap.get(field);
    }


    public void put(SearchField field, ConditionValueContainer values)
    {
        if(field == null)
        {
            throw new IllegalArgumentException("Field can not be null.");
        }
        this.fieldValueMap.put(field, values);
    }


    public ConditionValueContainer remove(SearchField field)
    {
        return this.fieldValueMap.remove(field);
    }


    public boolean hasValues()
    {
        return hasValues(false);
    }


    public boolean hasValues(boolean ignoreSort)
    {
        return (!this.fieldValueMap.isEmpty() || (!ignoreSort && this.sortProperty != null));
    }


    public ObjectTemplate getSelectedType()
    {
        return this.type;
    }


    public void setSelectedType(ObjectTemplate type)
    {
        this.type = type;
    }


    public boolean isExcludeSubtypes()
    {
        return this.excludeSubtypes;
    }


    public void setExcludeSubtypes(boolean checked)
    {
        this.excludeSubtypes = checked;
    }
}
