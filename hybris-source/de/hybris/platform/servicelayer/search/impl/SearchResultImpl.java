package de.hybris.platform.servicelayer.search.impl;

import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import java.util.List;

public class SearchResultImpl<E> implements SearchResult<E>
{
    private final int requestedCount;
    private final int requestedStart;
    private final List<E> resultList;
    private final List<E> unmodifiableResultList;
    private final int totalCount;
    private final String dataSourceId;
    private boolean fromCache;


    public SearchResultImpl(List<E> result, int totalCount, int requestedCount, int requestedStart)
    {
        this(result, totalCount, requestedCount, requestedStart, "undefined");
    }


    public SearchResultImpl(List<E> result, int totalCount, int requestedCount, int requestedStart, String dataSourceId)
    {
        this.resultList = result;
        this.unmodifiableResultList = (result == null) ? null : Collections.<E>unmodifiableList(result);
        this.totalCount = totalCount;
        this.requestedCount = requestedCount;
        this.requestedStart = requestedStart;
        this.dataSourceId = dataSourceId;
    }


    public int getCount()
    {
        return this.resultList.size();
    }


    public int getRequestedCount()
    {
        return this.requestedCount;
    }


    public int getRequestedStart()
    {
        return this.requestedStart;
    }


    public List<E> getResult()
    {
        return this.unmodifiableResultList;
    }


    public int getTotalCount()
    {
        return this.totalCount;
    }


    public boolean isFromCache()
    {
        return this.fromCache;
    }


    public void setFromCache(boolean fromCache)
    {
        this.fromCache = fromCache;
    }


    public String getDataSourceId()
    {
        return this.dataSourceId;
    }
}
