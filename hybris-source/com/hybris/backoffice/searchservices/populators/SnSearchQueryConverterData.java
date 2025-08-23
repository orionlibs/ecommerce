package com.hybris.backoffice.searchservices.populators;

import com.hybris.cockpitng.search.data.SearchQueryData;

public class SnSearchQueryConverterData
{
    private SearchQueryData searchQueryData;
    private int pageSize;
    private int offset;


    public SnSearchQueryConverterData()
    {
    }


    public SnSearchQueryConverterData(SearchQueryData searchQueryData, int pageSize, int offset)
    {
        this.searchQueryData = searchQueryData;
        this.pageSize = pageSize;
        this.offset = offset;
    }


    public void setSearchQueryData(SearchQueryData searchQueryData)
    {
        this.searchQueryData = searchQueryData;
    }


    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }


    public void setOffset(int offset)
    {
        this.offset = offset;
    }


    public SearchQueryData getSearchQueryData()
    {
        return this.searchQueryData;
    }


    public int getPageSize()
    {
        return this.pageSize;
    }


    public int getOffset()
    {
        return this.offset;
    }
}
