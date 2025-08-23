/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.selectivesync.tree;

import com.hybris.backoffice.widgets.selectivesync.renderer.SelectiveSyncRenderer;
import java.util.Objects;

/** Context for filtering tree in {@link SelectiveSyncRenderer}. */
public class FilterContext
{
    private String filterQuery = "";
    private boolean showIncluded = true;
    private boolean showNotIncluded = true;


    public FilterContext()
    {
    }


    /**
     * Creates a new instance.
     *
     * @param filterQuery
     *           filter query
     * @param showIncluded
     *           show attributes included in synchronization
     * @param showNotIncluded
     *           don't show attributes included in synchronization
     */
    public FilterContext(final String filterQuery, final boolean showIncluded, final boolean showNotIncluded)
    {
        this.filterQuery = filterQuery;
        this.showIncluded = showIncluded;
        this.showNotIncluded = showNotIncluded;
    }


    public String getFilterQuery()
    {
        return filterQuery;
    }


    public void setFilterQuery(final String filterQuery)
    {
        this.filterQuery = filterQuery;
    }


    public boolean getShowIncluded()
    {
        return showIncluded;
    }


    public void setShowIncluded(final boolean showIncluded)
    {
        this.showIncluded = showIncluded;
    }


    public boolean getShowNotIncluded()
    {
        return showNotIncluded;
    }


    public void setShowNotIncluded(final boolean showNotIncluded)
    {
        this.showNotIncluded = showNotIncluded;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || this.getClass() != o.getClass())
        {
            return false;
        }
        final FilterContext that = (FilterContext)o;
        return showIncluded == that.showIncluded && showNotIncluded == that.showNotIncluded
                        && Objects.equals(filterQuery, that.filterQuery);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(filterQuery, showIncluded, showNotIncluded);
    }
}
