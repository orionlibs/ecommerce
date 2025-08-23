package com.hybris.backoffice.searchservices.populators;

import de.hybris.platform.searchservices.search.data.SnSearchResult;

public class SnSearchResultSourceData
{
    private SnSearchResult snSearchResult;
    private String typeCode;


    public SnSearchResultSourceData(SnSearchResult snSearchResult, String typeCode)
    {
        this.snSearchResult = snSearchResult;
        this.typeCode = typeCode;
    }


    public SnSearchResult getSnSearchResult()
    {
        return this.snSearchResult;
    }


    public void setSnSearchResult(SnSearchResult snSearchResult)
    {
        this.snSearchResult = snSearchResult;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }
}
