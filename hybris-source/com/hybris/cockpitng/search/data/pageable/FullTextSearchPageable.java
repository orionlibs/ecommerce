/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data.pageable;

import com.hybris.cockpitng.search.data.FullTextSearchData;

/**
 *
 * Pageable with auto correction (spell check) and facets.
 */
public interface FullTextSearchPageable<R> extends Pageable<R>
{
    /**
     * Gets full text search data.
     *
     * @return full text search data.
     */
    FullTextSearchData getFullTextSearchData();


    /**
     * Performs additional operations when {@link Pageable#getCurrentPage()} is called
     */
    default void onPageLoaded()
    {
        // NOOP
    }
}
