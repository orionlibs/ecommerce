/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSearchInitContext
{
    public static final String ADVANCED_SEARCH_DATA = "advanced_search_data";
    public static final String ADVANCED_SEARCH_CONFIG = "advanced_search_config";
    public static final String DISABLE_SIMPLE_SEARCH = "disableSimpleSearch";
    public static final String INITIAL_TYPE_CODE = "initialTypeCode";
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "$class")
    @JsonIgnore
    private final Map<String, Object> attributes = new HashMap<>();


    public AdvancedSearchInitContext(final AdvancedSearchData searchData)
    {
        this(searchData, null);
    }


    @JsonCreator
    public AdvancedSearchInitContext(@JsonProperty("advancedSearchData") final AdvancedSearchData searchData,
                    @JsonProperty("advancedSearchConfig") final AdvancedSearch advancedSearchConfig)
    {
        attributes.put(ADVANCED_SEARCH_DATA, searchData);
        attributes.put(ADVANCED_SEARCH_CONFIG, advancedSearchConfig);
    }


    @JsonProperty("advancedSearchConfig")
    public AdvancedSearch getAdvancedSearchConfig()
    {
        AdvancedSearch ret = null;
        final Object potentialConfig = attributes.get(ADVANCED_SEARCH_CONFIG);
        if(potentialConfig instanceof AdvancedSearch)
        {
            ret = (AdvancedSearch)potentialConfig;
        }
        return ret;
    }


    @JsonProperty("advancedSearchData")
    public AdvancedSearchData getAdvancedSearchData()
    {
        AdvancedSearchData ret = null;
        final Object potentialData = attributes.get(ADVANCED_SEARCH_DATA);
        if(potentialData instanceof AdvancedSearchData)
        {
            ret = (AdvancedSearchData)potentialData;
        }
        return ret;
    }


    @JsonProperty("initialTypeCode")
    public String getInitialTypeCode()
    {
        final Object initialTypeCode = attributes.get(INITIAL_TYPE_CODE);
        if(initialTypeCode instanceof String)
        {
            return (String)initialTypeCode;
        }
        return null;
    }


    public void setInitialTypeCode(final String initialTypeCode)
    {
        attributes.put(INITIAL_TYPE_CODE, initialTypeCode);
    }


    public void addAttribute(final String key, final Object value)
    {
        attributes.put(key, value);
    }


    public Object getAttribute(final String key)
    {
        return attributes.get(key);
    }


    public Object removeAttribute(final String key)
    {
        Object ret = null;
        if(attributes.containsKey(key))
        {
            ret = attributes.remove(key);
        }
        return ret;
    }
}
