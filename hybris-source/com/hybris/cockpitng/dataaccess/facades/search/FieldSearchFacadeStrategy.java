/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.search;

import com.hybris.cockpitng.dataaccess.context.Context;

/**
 * Represents a strategy that is used by {@link FieldSearchFacade}
 */
public interface FieldSearchFacadeStrategy<T> extends FieldSearchFacade<T>
{
    String CONTEXT_ORIGINAL_QUERY = "originalQuery";


    /**
     * Returns true, if this strategy can handle given <code>typeCode</code>, otherwise false.
     *
     * @param typeCode represents a type identifier
     */
    boolean canHandle(String typeCode);


    /**
     * Returns true, if this strategy can handle given <code>typeCode</code>, otherwise false.
     *
     * @param typeCode represents a type identifier
     * @param context represents additional context for making the decision
     */
    default boolean canHandle(final String typeCode, final Context context)
    {
        return canHandle(typeCode);
    }


    /**
     * Returns unique logical strategy name allowing resolving this strategy by name from the strategy registry
     *
     * @return strategy name
     */
    default String getStrategyName()
    {
        return getClass().getName();
    }


    /**
     * Returns if need set global operator to OR for current strategy registry in simple search mode
     *
     */
    default boolean useOrForGlobalOperator()
    {
        return false;
    }
}
