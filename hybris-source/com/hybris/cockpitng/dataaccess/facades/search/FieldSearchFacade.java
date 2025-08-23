/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.search;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;

/**
 * Facade for condition based search.
 *
 * @param <T>
 *           type enclosed in search results
 */
public interface FieldSearchFacade<T>
{
    /**
     * Takes the search query data (type, attributes, sort, sort order, search text ) and returns a
     * {@link com.hybris.cockpitng.search.data.pageable.Pageable} object that allows paging the resulting objects.
     *
     * @param searchQueryData {@link com.hybris.cockpitng.search.data.SearchQueryData}
     * @return {@link com.hybris.cockpitng.search.data.pageable.Pageable}
     */
    Pageable<T> search(SearchQueryData searchQueryData);


    /**
     * Takes the search query data (type, attributes, sort, sort order, search text ) and returns a
     * {@link com.hybris.cockpitng.search.data.pageable.Pageable} object that allows paging the resulting objects.
     *
     * @param context context of search operation {@link Context}
     * @param searchQueryData {@link com.hybris.cockpitng.search.data.SearchQueryData}
     * @return {@link com.hybris.cockpitng.search.data.pageable.Pageable}
     */
    default Pageable<T> search(final SearchQueryData searchQueryData, final Context context)
    {
        return search(searchQueryData);
    }


    /**
     * Informs if attributeQualifier of given type is sortable
     *
     * @param type of data
     * @param attributeQualifier to check
     * @param context
     *           context of operation {@link Context}
     * @return whether attributeQualifier is sortable or not
     */
    default boolean isSortable(final DataType type, final String attributeQualifier, final Context context)
    {
        return false;
    }
}
