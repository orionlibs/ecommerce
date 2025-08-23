/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data.facet;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * This class defines the root facet. For example, <b>hardware</b>(FacetData) contains [cpu, monitor,
 * memory](FacetValueData).
 */
public class FacetData implements Comparable<FacetData>
{
    private final String name;
    private final String displayName;
    private final FacetType facetType;
    private final Map<String, FacetValueData> facetValues;


    public FacetData(final String name, final String displayName, final FacetType facetType,
                    final List<FacetValueData> facetValues)
    {
        this.facetType = facetType;
        if(name == null)
        {
            throw new IllegalArgumentException("name must not be null");
        }
        if(facetValues == null)
        {
            throw new IllegalArgumentException("facetValues must not be null");
        }
        this.name = name;
        this.displayName = StringUtils.defaultIfBlank(displayName, name);
        this.facetValues = facetValues.stream()
                        .collect(Collectors.toMap(FacetValueData::getName, Function.identity(), (f1, f2) -> f2, LinkedHashMap::new));
    }


    public void addFacetValue(final FacetValueData facetValue)
    {
        facetValues.put(facetValue.getName(), facetValue);
    }


    public Collection<FacetValueData> getFacetValues()
    {
        return facetValues.values();
    }


    public Collection<String> getFacetValueNames()
    {
        return facetValues.keySet();
    }


    public FacetValueData getFacetValue(final String name)
    {
        if(name == null)
        {
            throw new IllegalArgumentException("name must not be null");
        }
        return facetValues.get(name);
    }


    public String getName()
    {
        return name;
    }


    public String getDisplayName()
    {
        return displayName;
    }


    public FacetType getFacetType()
    {
        return facetType;
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        final FacetData other = (FacetData)obj;
        if(name == null)
        {
            return other.name == null;
        }
        else
        {
            return name.equals(other.name);
        }
    }


    @Override
    public int compareTo(final FacetData other)
    {
        return name.compareTo(other.name);
    }
}
