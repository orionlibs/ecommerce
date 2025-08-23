/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.search.builder;

import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacadeStrategy;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryData;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import java.util.Collections;
import java.util.List;

/**
 * An interface represents condition query builder that is used by {@link FieldSearchFacadeStrategy}.
 * </p>
 */
public interface ConditionQueryBuilder
{
    List<GenericCondition> buildQuery(final GenericQuery query, String typeCode, SearchAttributeDescriptor attribute,
                    final SearchQueryData searchQueryData);


    default List<GenericCondition> buildQuery(final GenericQuery query, final String typeCode,
                    final SearchQueryCondition condition, final SearchQueryData searchQueryData)
    {
        return Collections.emptyList();
    }
}
