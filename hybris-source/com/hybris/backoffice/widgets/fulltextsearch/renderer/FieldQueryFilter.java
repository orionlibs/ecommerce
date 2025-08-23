/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.fulltextsearch.renderer;

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;

/**
 * Extended data about Fulltext search. Standard POJO of FulltextSearch has been extended by values required by filter
 * field renderers.
 *
 * @see com.hybris.backoffice.widgets.fulltextsearch.renderer.impl.DefaultFieldQueryFieldRenderer
 */
public class FieldQueryFilter extends FullTextSearchFilter
{
    private final String filterId;
    private final AdvancedSearchData searchData;
    private final boolean applied;


    public FieldQueryFilter(final String filterId, final AdvancedSearchData searchData, final String qualifier)
    {
        super(qualifier);
        this.filterId = filterId;
        this.searchData = searchData;
        this.applied = false;
    }


    public FieldQueryFilter(final String filterId, final AdvancedSearchData searchData, final FullTextSearchFilter filter)
    {
        super(filter.getName());
        this.filterId = filterId;
        this.searchData = searchData;
        this.applied = true;
        FieldQueryFilter.this.setEnabled(filter.isEnabled());
        FieldQueryFilter.this.setValue(filter.getValue());
        FieldQueryFilter.this.setLocale(filter.getLocale());
        FieldQueryFilter.this.setOperator(filter.getOperator());
    }


    /**
     *
     * @return search data provided by end user
     */
    public AdvancedSearchData getSearchData()
    {
        return searchData;
    }


    /**
     *
     * @return <code>true</code> if filter has already been applied at least once
     */
    public boolean isApplied()
    {
        return applied;
    }


    /**
     *
     * @return unique identity of a filter
     */
    public String getFilterId()
    {
        return filterId;
    }
}
