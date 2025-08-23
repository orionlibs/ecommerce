package com.hybris.datahub;

import java.util.List;

public class PagedDataHubResponse<T>
{
    final long totalCount;
    final List<T> items;


    public PagedDataHubResponse(long totalCount, List<T> items)
    {
        this.totalCount = totalCount;
        this.items = items;
    }


    public long getTotalCount()
    {
        return this.totalCount;
    }


    public List<T> getItems()
    {
        return this.items;
    }
}
