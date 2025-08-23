/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import java.util.Optional;

/**
 * Initializes Advanced Search
 */
public interface AdvancedSearchInitializer
{
    /**
     * Add search conditions to passed {@link AdvancedSearchData} object. {@link NavigationNode} object can be passed to
     * give context.
     *
     * @param searchData
     * @param navigationNode
     */
    void addSearchDataConditions(final AdvancedSearchData searchData, final Optional<NavigationNode> navigationNode);
}
