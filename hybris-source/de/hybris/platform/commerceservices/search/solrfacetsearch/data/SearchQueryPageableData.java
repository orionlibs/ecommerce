package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import java.io.Serializable;

public class SearchQueryPageableData<STATE> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private STATE searchQueryData;
    private PageableData pageableData;


    public void setSearchQueryData(STATE searchQueryData)
    {
        this.searchQueryData = searchQueryData;
    }


    public STATE getSearchQueryData()
    {
        return this.searchQueryData;
    }


    public void setPageableData(PageableData pageableData)
    {
        this.pageableData = pageableData;
    }


    public PageableData getPageableData()
    {
        return this.pageableData;
    }
}
