/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.fulltextsearch;

import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Strategy to be used by Fulltext Search
 */
public interface FullTextSearchStrategy
{
    /**
     * Looks for a platform type code of a value for filter of specified fieldName.
     *
     * @param typeCode
     *           type code of values to be filtered
     * @param fieldName
     *           name of a field for which operators are to be provided
     * @return type code of a value of filter or <code>null</code> if unknown
     */
    String getFieldType(final String typeCode, final String fieldName);


    /**
     * Checks if filter of specified fieldName supports localized values.
     *
     * @param typeCode
     *           type code of values to be filtered
     * @param fieldName
     *           fieldName of a field for which operators are to be provided
     * @return <code>true</code> if filter is localized
     */
    boolean isLocalized(final String typeCode, final String fieldName);


    /**
     * Returns a list of languages by which it is possible to search on a given type.
     *
     * @param typeCode
     *           type code of values to be filtered
     * @return list of languages in the form of ISO codes
     */
    default Collection<String> getAvailableLanguages(final String typeCode)
    {
        return Arrays.stream(Locale.getAvailableLocales()).map(Locale::getLanguage).collect(Collectors.toList());
    }


    /**
     * Looks for all available compare operators for filter of specified field name.
     *
     * @param typeCode
     *           type code of values to be filtered
     * @param fieldName
     *           name of a field for which operators are to be provided
     * @return collection of supported operators
     */
    Collection<ValueComparisonOperator> getAvailableOperators(final String typeCode, final String fieldName);


    /**
     * Returns name of the search strategy which can be used in configuration of preferred search strategy
     *
     * @return name of the search strategy
     */
    default String getStrategyName()
    {
        return getClass().getName();
    }
}
