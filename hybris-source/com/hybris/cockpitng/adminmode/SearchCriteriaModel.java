/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.adminmode;

import com.hybris.cockpitng.core.config.impl.DefaultCockpitConfigurationService;
import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Stores current search criteria and available attribute values
 */
public class SearchCriteriaModel
{
    private final Map<String, String> searchCriteria = new HashMap<>();
    private final FilteredContext filteredContext;
    private final FilterOptionsProvider filterOptionsProvider;
    private final List<SearchCriteriaModelObserver> observers = new ArrayList<>();
    private Map<String, Set<String>> availableAttributeValues = new HashMap<>();


    public SearchCriteriaModel(final DefaultCockpitConfigurationService service)
    {
        filteredContext = new FilteredContext();
        filterOptionsProvider = new FilterOptionsProvider(service, filteredContext);
        update(null);
    }


    public List<Context> getFilteredContext()
    {
        return filteredContext.getFilteredContextList();
    }


    public void addSearchCriteria(final String attributeName, final String attributeValue)
    {
        searchCriteria.put(attributeName, attributeValue);
        update(attributeName);
    }


    public Map<String, String> getSearchCriteria()
    {
        return searchCriteria;
    }


    public void removeSearchCriteria(final String attributeName)
    {
        if(searchCriteria.containsKey(attributeName))
        {
            searchCriteria.remove(attributeName);
            update(attributeName);
        }
    }


    public boolean hasAnySearchCriteria()
    {
        return !searchCriteria.isEmpty();
    }


    public boolean hasSearchCriteriaForAttribute(final String attributeName)
    {
        return searchCriteria.containsKey(attributeName);
    }


    public Set<String> getAvailableAttributeValuesForAttribute(final String attributeName)
    {
        return availableAttributeValues.get(attributeName);
    }


    public List<String> getAllAttributeNames()
    {
        return new ArrayList<>(availableAttributeValues.keySet());
    }


    private void update(final String changedAttribute)
    {
        availableAttributeValues = filterOptionsProvider.getAvailableAttributeValues(searchCriteria);
        observers.forEach(obs -> obs.notifyChanged(changedAttribute));
    }


    public void addObserver(final SearchCriteriaModelObserver observer)
    {
        observers.add(observer);
    }


    public void clearObservers()
    {
        observers.clear();
    }
}
