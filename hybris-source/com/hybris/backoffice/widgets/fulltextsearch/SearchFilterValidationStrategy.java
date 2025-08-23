/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.fulltextsearch;

import com.hybris.cockpitng.search.data.ValueComparisonOperator;

/**
 * Strategy checking if search filter is valid.
 */
public interface SearchFilterValidationStrategy
{
    /**
     * Checks if filter of given name is valid for use.
     *
     * @param typeCode
     *           of item that filter corresponds to
     * @param name
     *           of filter property
     * @param value
     *           of filter
     * @return true if filter is valid
     */
    boolean isValid(final String typeCode, final String name, final Object value);


    /**
     * Checks if filter of given name is valid for use.
     *
     * @param typeCode
     *           of item that filter corresponds to
     * @param name
     *           of filter property
     * @param value
     *           of filter
     * @param operator
     * 			 filter operator
     * @return true if filter is valid
     */
    default boolean isValid(final String typeCode, final String name, final Object value, final ValueComparisonOperator operator)
    {
        return isValid(typeCode, name, value);
    }


    /**
     * Checks if strategy can be used in terms of specific search strategy.
     *
     * @param searchStrategy
     *           search strategy name
     * @return true if filter validation strategy can handle search strategy
     */
    boolean canHandle(final String searchStrategy);
}
