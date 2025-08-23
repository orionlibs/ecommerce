/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple pojo class that agregates some items with additional information about their type.
 * Note:
 * The reference object can be either <b>single</b> object or any <b>collection</b>.
 */
public class TypeAwareSelectionContext<T extends Object>
{
    private String typeCode;
    private T selectedItem;
    private List<T> availableItems;
    private boolean multiSelect;
    public static final String SEARCH_CTX_PARAM = "searchCtx";
    private final Map<String, Object> parameters = new HashMap<>();


    @JsonCreator
    public TypeAwareSelectionContext(@JsonProperty("typeCode") final String typeCode, @JsonProperty("selected") final T selected,
                    @JsonProperty("availableItems") final List<T> availableItems)
    {
        this.typeCode = typeCode;
        this.selectedItem = selected;
        this.availableItems = availableItems;
    }


    public TypeAwareSelectionContext(final T selectedItem, final List<T> availableItems)
    {
        this.selectedItem = selectedItem;
        this.availableItems = availableItems;
    }


    public Object getSelectedItem()
    {
        return this.selectedItem;
    }


    public String getTypeCode()
    {
        return typeCode;
    }


    public List<T> getAvailableItems()
    {
        return availableItems;
    }


    public void setTypeCode(final String typeCode)
    {
        this.typeCode = typeCode;
    }


    public void setSelectedItem(final T selectedItem)
    {
        this.selectedItem = selectedItem;
    }


    public void setAvailableItems(final List<T> availableItems)
    {
        this.availableItems = availableItems;
    }


    public Map<String, Object> getParameters()
    {
        return Collections.unmodifiableMap(parameters);
    }


    public void addParameter(final String key, final Object value)
    {
        parameters.put(key, value);
    }


    public boolean isMultiSelect()
    {
        return multiSelect;
    }


    public void setMultiSelect(final boolean multiSelect)
    {
        this.multiSelect = multiSelect;
    }
}
