/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data.pageable;

import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.search.data.IdentifiableSearchQuery;
import com.hybris.cockpitng.search.data.SortData;
import java.util.List;

/**
 * Capable of paging the collections of result type <code>R</code>.
 */
public interface Pageable<R> extends IdentifiableSearchQuery
{
    /**
     * Gets the entries of the current record.
     *
     * @return all entries of the current record page
     */
    List<R> getCurrentPage();


    /**
     * Refreshes current page.
     */
    void refresh();


    /**
     * Gets the entries of the next record page and updates the current page number (see {@link #getCurrentPage()}).
     *
     * @return all entries of the next record page or an empty list if {@link #hasNextPage()} returns <p>false</p>
     */
    List<R> nextPage();


    /**
     * Get the entries of the previous page and updates the current page number (see {@link #getCurrentPage()}).
     *
     * @return List of <code>RESULT</code> or empty list if {@link #hasPreviousPage()} returns false.
     */
    List<R> previousPage();


    /**
     * Determines if there is a next page.
     *
     * @return boolean
     */
    boolean hasNextPage();


    /**
     * Determines if there is a previous page.
     *
     * @return boolean
     */
    boolean hasPreviousPage();


    /**
     * Returns the page size.
     *
     * @return integer
     */
    int getPageSize();


    /**
     * Returns the "common" type code of all entries.
     *
     * @return the common type code or <p>null</p> if no common type code can be applied
     */
    String getTypeCode();


    /**
     * Changes the page size and returns the current page with a new page size.
     *
     * @param pageSize
     *           - new page size
     * @return {@link List} of <code>RESULT</code> or empty list.
     */
    List<R> setPageSize(int pageSize);


    /**
     * Returns count of all items in the collection that is being paged.
     *
     * @return total items count
     */
    int getTotalCount();


    /**
     * Returns the active page number.
     *
     * @return the active page number
     */
    int getPageNumber();


    /**
     * Sets the active page number.
     */
    void setPageNumber(int pageNo);


    /**
     * Returns sort related information
     *
     * @return sort related information
     */
    SortData getSortData();


    /**
     * @param sortData
     *           sort related information
     */
    void setSortData(SortData sortData);


    /**
     * Returns all results on <b>all</b> pages. May be a long lasting operation for big data sets, therefore should be used
     * carefully.
     *
     * @return
     */
    List<R> getAllResults();


    /**
     * @param queryId
     *           Queries with same identifiers are assumed to perform same request to data source
     */
    default void setQueryId(final String queryId)
    {
    }


    /**
     * @param event
     * 			Event for trigger current Pageable action, such as: Delete item
     */
    default void setCockpitEvent(final CockpitEvent event)
    {
    }
}
