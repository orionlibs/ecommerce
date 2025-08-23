package de.hybris.platform.commerceservices.search.searchservices.data;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import java.io.Serializable;

public class SnSearchQueryConverterData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String indexTypeId;
    private SolrSearchQueryData searchQuery;
    private PageableData pageable;


    public void setIndexTypeId(String indexTypeId)
    {
        this.indexTypeId = indexTypeId;
    }


    public String getIndexTypeId()
    {
        return this.indexTypeId;
    }


    public void setSearchQuery(SolrSearchQueryData searchQuery)
    {
        this.searchQuery = searchQuery;
    }


    public SolrSearchQueryData getSearchQuery()
    {
        return this.searchQuery;
    }


    public void setPageable(PageableData pageable)
    {
        this.pageable = pageable;
    }


    public PageableData getPageable()
    {
        return this.pageable;
    }
}
