package de.hybris.platform.commerceservices.search.searchservices.data;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import java.io.Serializable;

public class SnSearchResultConverterData<R> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private SnIndexConfiguration indexConfiguration;
    private SnIndexType indexType;
    private SolrSearchQueryData searchQuery;
    private R snSearchResult;


    public void setIndexConfiguration(SnIndexConfiguration indexConfiguration)
    {
        this.indexConfiguration = indexConfiguration;
    }


    public SnIndexConfiguration getIndexConfiguration()
    {
        return this.indexConfiguration;
    }


    public void setIndexType(SnIndexType indexType)
    {
        this.indexType = indexType;
    }


    public SnIndexType getIndexType()
    {
        return this.indexType;
    }


    public void setSearchQuery(SolrSearchQueryData searchQuery)
    {
        this.searchQuery = searchQuery;
    }


    public SolrSearchQueryData getSearchQuery()
    {
        return this.searchQuery;
    }


    public void setSnSearchResult(R snSearchResult)
    {
        this.snSearchResult = snSearchResult;
    }


    public R getSnSearchResult()
    {
        return this.snSearchResult;
    }
}
