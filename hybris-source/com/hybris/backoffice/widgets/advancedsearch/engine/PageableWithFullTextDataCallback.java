/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch.engine;

import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.pageable.FullTextSearchPageable;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import java.util.List;

/**
 * Pageable wrapper which calls onFullTextSearchData(FullTextSearchData) after current page is fetched and full
 * text search data is available.
 */
public abstract class PageableWithFullTextDataCallback implements FullTextSearchPageable
{
    private final FullTextSearchPageable pageable;
    private String queryId;


    PageableWithFullTextDataCallback(final FullTextSearchPageable pageable)
    {
        this.pageable = pageable;
    }


    @Override
    public List getCurrentPage()
    {
        return pageable.getCurrentPage();
    }


    public Pageable getPageable()
    {
        return pageable;
    }


    @Override
    public void refresh()
    {
        pageable.refresh();
    }


    @Override
    public List nextPage()
    {
        return pageable.nextPage();
    }


    @Override
    public List previousPage()
    {
        return pageable.previousPage();
    }


    @Override
    public boolean hasNextPage()
    {
        return pageable.hasNextPage();
    }


    @Override
    public boolean hasPreviousPage()
    {
        return pageable.hasPreviousPage();
    }


    @Override
    public int getPageSize()
    {
        return pageable.getPageSize();
    }


    @Override
    public String getTypeCode()
    {
        return pageable.getTypeCode();
    }


    @Override
    public List setPageSize(final int pageSize)
    {
        return pageable.setPageSize(pageSize);
    }


    @Override
    public int getTotalCount()
    {
        return pageable.getTotalCount();
    }


    @Override
    public int getPageNumber()
    {
        return pageable.getPageNumber();
    }


    @Override
    public void setPageNumber(final int pageNo)
    {
        pageable.setPageNumber(pageNo);
    }


    @Override
    public SortData getSortData()
    {
        return pageable.getSortData();
    }


    @Override
    public void setSortData(final SortData sortData)
    {
        pageable.setSortData(sortData);
    }


    @Override
    public List getAllResults()
    {
        return pageable.getAllResults();
    }


    @Override
    public FullTextSearchData getFullTextSearchData()
    {
        return pageable.getFullTextSearchData();
    }


    @Override
    public String getQueryId()
    {
        return queryId;
    }


    @Override
    public void setQueryId(final String queryId)
    {
        this.queryId = queryId;
    }
}
