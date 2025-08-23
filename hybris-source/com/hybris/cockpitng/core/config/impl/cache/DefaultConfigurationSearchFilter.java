/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Default implementation of search filter.
 * <P>
 * Filter gathers all possible values for different attributes and checks if provided value matches any of them if
 * requested.
 */
public class DefaultConfigurationSearchFilter implements ConfigurationSearchFilter
{
    private final Map<String, Collection<String>> possibilities;
    private final ContextAttributeComparator comparator;


    /**
     *
     * @param comparator comparator used to check whether provided value matches any of possible
     */
    public DefaultConfigurationSearchFilter(final ContextAttributeComparator comparator)
    {
        this.comparator = comparator;
        possibilities = new HashMap<>();
    }


    /**
     * Adds possible values for different attributes.
     *
     * @param values map of possible values in convention &lt;attribute name, collection of values&gt;
     */
    public void addPossibleValues(final Map<String, ? extends Collection<String>> values)
    {
        values.forEach(this::addPossibleValues);
    }


    /**
     * Adds possible values for a single attribute.
     *
     * @param attribute attribute name
     * @param values possible values
     */
    public void addPossibleValues(final String attribute, final Collection<String> values)
    {
        Collection<String> possibleValues = possibilities.get(attribute);
        if(possibleValues == null)
        {
            possibleValues = new HashSet<>();
            possibilities.put(attribute, possibleValues);
        }
        possibleValues.addAll(values);
    }


    /**
     * Gets all defined possible values for specified attribute.
     *
     * @param attribute attribute name
     * @return collection of possible values for attribute
     */
    public Collection<String> getPossibleValues(final String attribute)
    {
        final Collection<String> values = possibilities.get(attribute);
        return values != null ? Collections.unmodifiableCollection(values) : Collections.emptySet();
    }


    @Override
    public boolean filter(final String name, final String value)
    {
        return StringUtils.isNotBlank(value)
                        && !getPossibleValues(name).stream().anyMatch(possibility -> comparator.matches(name, possibility, value));
    }
}
