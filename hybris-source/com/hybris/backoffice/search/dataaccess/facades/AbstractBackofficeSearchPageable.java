/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.search.dataaccess.facades;

import com.hybris.cockpitng.search.data.FullTextSearchData;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.pageable.FullTextSearchPageable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;

public abstract class AbstractBackofficeSearchPageable<P> implements FullTextSearchPageable<P>
{
    protected final SearchQueryData searchQueryData;
    protected List<P> currentPageCache;
    protected int pageSize;
    protected int totalCount = 0;
    protected int currentStart = 0;
    protected boolean initialized;
    protected String typeCode;
    protected FullTextSearchData fullTextSearchData;


    public AbstractBackofficeSearchPageable(final SearchQueryData searchQueryData)
    {
        if(searchQueryData != null)
        {
            typeCode = searchQueryData.getSearchType();
            pageSize = searchQueryData.getPageSize();
        }
        this.searchQueryData = searchQueryData;
    }


    protected void initialize()
    {
        if(!initialized)
        {
            getCurrentPage();
            initialized = true;
        }
    }


    @Override
    public List<P> getCurrentPage()
    {
        if(currentPageCache == null)
        {
            final Optional<List<P>> notEmptyResult = getCurrentNotEmptyPage();
            final List<P> result = notEmptyResult.orElse(Lists.emptyList());
            initialized = true;
            cacheCurrentPage(result);
            return result;
        }
        return currentPageCache;
    }


    protected Optional<List<P>> getCurrentNotEmptyPage()
    {
        do
        {
            final List<P> result = getResults(pageSize, currentStart);
            if(!result.isEmpty())
            {
                return Optional.of(result);
            }
        }
        while(--currentStart >= 0);
        currentStart = 0;
        totalCount = 0;
        return Optional.empty();
    }


    protected abstract List<P> getResults(final int pageSize, final int offset);


    private void cacheCurrentPage(final List<P> result)
    {
        currentPageCache = result;
    }


    @Override
    public void refresh()
    {
        invalidateCurrentPageCache();
        initialized = false;
    }


    protected void invalidateCurrentPageCache()
    {
        currentPageCache = null;
    }


    @Override
    public int getPageSize()
    {
        return pageSize;
    }


    @Override
    public String getTypeCode()
    {
        return typeCode;
    }


    @Override
    public boolean hasNextPage()
    {
        if(pageSize <= 0)
        {
            return false;
        }
        initialize();
        return totalCount > (currentStart + pageSize);
    }


    @Override
    public List<P> nextPage()
    {
        if(hasNextPage())
        {
            currentStart += pageSize;
            invalidateCurrentPageCache();
            return getCurrentPage();
        }
        return Collections.emptyList();
    }


    @Override
    public boolean hasPreviousPage()
    {
        initialize();
        return currentStart > 0;
    }


    @Override
    public List<P> previousPage()
    {
        if(hasPreviousPage())
        {
            currentStart -= pageSize;
            if(currentStart < 0)
            {
                currentStart = 0;
            }
            invalidateCurrentPageCache();
            return getCurrentPage();
        }
        return Collections.emptyList();
    }


    @Override
    public List<P> setPageSize(final int pageSize)
    {
        if(this.pageSize != pageSize)
        {
            this.pageSize = pageSize;
            invalidateCurrentPageCache();
        }
        return getCurrentPage();
    }


    @Override
    public int getTotalCount()
    {
        initialize();
        return totalCount;
    }


    @Override
    public int getPageNumber()
    {
        initialize();
        return currentStart;
    }


    @Override
    public void setPageNumber(final int pageNo)
    {
        initialize();
        if(pageNo != currentStart)
        {
            currentStart = pageNo;
            invalidateCurrentPageCache();
        }
    }


    @Override
    public SortData getSortData()
    {
        return searchQueryData.getSortData();
    }


    @Override
    public void setSortData(final SortData sortData)
    {
        searchQueryData.setSortData(sortData);
        invalidateCurrentPageCache();
    }


    @Override
    public List<P> getAllResults()
    {
        final int zeroOffset = 0;
        return getResults(getTotalCount(), zeroOffset);
    }


    @Override
    public FullTextSearchData getFullTextSearchData()
    {
        return fullTextSearchData;
    }
}
