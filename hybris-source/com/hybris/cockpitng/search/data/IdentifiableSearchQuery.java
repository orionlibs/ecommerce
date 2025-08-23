/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data;

public interface IdentifiableSearchQuery
{
    /**
     * @return identifier of a query. Queries with same identifiers are assumed to perform same request to data source
     */
    default String getQueryId()
    {
        return String.valueOf(this.hashCode());
    }
}
