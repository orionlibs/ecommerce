/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Collection;

/**
 * A service responsible for looking up appropriate search operators for given atrribute {@link DataAttribute}
 */
public interface AdvancedSearchOperatorService
{
    /**
     * Looks up all available search operators for given attribute
     *
     * @param attribute an instance of {@link DataAttribute}
     * @return available search operators
     */
    Collection<ValueComparisonOperator> getAvailableOperators(final DataAttribute attribute);


    /**
     * Finds corresponding search operator by <i>operatorCode</i> for given attribute {@link DataAttribute}
     * @param attribute an instance of {@link DataAttribute}
     * @param operatorCode by which to search
     * @return comparison operator
     */
    ValueComparisonOperator findMatchingOperator(final DataAttribute attribute, final String operatorCode);
}
