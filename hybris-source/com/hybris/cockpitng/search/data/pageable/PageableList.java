/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data.pageable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.search.data.SortData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link Pageable} which can be instantiated from {@link List} and an integer value indicating the
 * page size.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class PageableList<R> implements Pageable<R>
{
    private static final int DEFAULT_PAGE_SIZE = 10;
    private final List<R> allResults;
    private int pageSize = DEFAULT_PAGE_SIZE;
    private boolean hasNext;
    private boolean hasPrev;
    private String typeCode;
    private int currentPageNo;
    private SortData sortData;
    private String queryId;


    /**
     * Creates a new {@link PageableList} based on the {@link List} and the pageSize.
     *
     * @param allResults
     *           - {@link List} of elements that you want to page.
     * @param pageSize
     */
    public PageableList(final List<R> allResults, final int pageSize)
    {
        this(allResults, pageSize, Object.class.getCanonicalName());
    }


    @JsonCreator
    public PageableList(@JsonProperty("allResults") final List<R> allResults, @JsonProperty("pageSize") final int pageSize,
                    @JsonProperty("typeCode") final String typeCode)
    {
        Validate.assertTrue("Page size must be a positiver number", pageSize > 0);
        this.allResults = new ArrayList<>(allResults);
        this.pageSize = pageSize;
        hasNext = allResults.size() > pageSize;
        this.typeCode = typeCode;
    }


    /**
     * Copy constructor. Creates deep copy of given <code>PageableList</code> object with shallow copy of
     * <code>allResults</code> list.
     *
     * @param source
     *           object to copy
     */
    public PageableList(final PageableList<R> source)
    {
        allResults = new ArrayList<>(source.allResults);
        pageSize = source.pageSize;
        hasNext = source.hasNext;
        hasPrev = source.hasPrev;
        typeCode = source.typeCode;
        currentPageNo = source.currentPageNo;
        if(source.sortData != null)
        {
            sortData = new SortData(source.sortData.getSortAttribute(), source.sortData.isAscending());
        }
    }


    @Override
    public void refresh()
    {
        updateCurrentWindow();
    }


    @Override
    public List<R> getCurrentPage()
    {
        return updateCurrentWindow();
    }


    private List<R> updateCurrentWindow()
    {
        final List<R> currentWindow;
        if(currentPageNo > ((allResults.size() - 1) / pageSize))
        {
            setPageNumber(currentPageNo - 1);
        }
        final int currentWindowEnd = (currentPageNo + 1) * pageSize;
        if(allResults.size() > currentWindowEnd)
        {
            currentWindow = Collections.unmodifiableList(allResults.subList(currentPageNo * pageSize, currentWindowEnd));
            hasNext = true;
        }
        else
        {
            currentWindow = Collections.unmodifiableList(allResults.subList(currentPageNo * pageSize, allResults.size()));
            hasNext = false;
        }
        hasPrev = currentPageNo > 0;
        return currentWindow;
    }


    @Override
    public List<R> nextPage()
    {
        if(hasNextPage())
        {
            currentPageNo++;
            return updateCurrentWindow();
        }
        return Collections.<R>emptyList();
    }


    @Override
    public List<R> previousPage()
    {
        if(hasPreviousPage())
        {
            currentPageNo--;
            return updateCurrentWindow();
        }
        return Collections.<R>emptyList();
    }


    @Override
    public boolean hasNextPage()
    {
        return hasNext;
    }


    @Override
    public boolean hasPreviousPage()
    {
        return hasPrev;
    }


    @Override
    public void setPageNumber(final int pageNo)
    {
        currentPageNo = pageNo;
    }


    @Override
    public int getPageSize()
    {
        return this.pageSize;
    }


    @Override
    public String getTypeCode()
    {
        return this.typeCode;
    }


    public void setTypeCode(final String typeCode)
    {
        this.typeCode = typeCode;
    }


    @Override
    public List<R> setPageSize(final int _pageSize)
    {
        this.pageSize = _pageSize;
        return getCurrentPage();
    }


    @Override
    public int getTotalCount()
    {
        return allResults.size();
    }


    @Override
    public int getPageNumber()
    {
        return this.currentPageNo;
    }


    @Override
    public SortData getSortData()
    {
        return sortData;
    }


    @Override
    public void setSortData(final SortData sortData)
    {
        this.sortData = sortData;
    }


    @Override
    public List<R> getAllResults()
    {
        return Collections.unmodifiableList(allResults);
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
