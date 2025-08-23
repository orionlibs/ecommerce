package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import java.io.Serializable;
import java.util.List;

public class SolrSearchQueryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String freeTextSearch;
    private String categoryCode;
    private List<SolrSearchQueryTermData> filterTerms;
    private List<SolrSearchFilterQueryData> filterQueries;
    private String sort;
    private SearchQueryContext searchQueryContext;


    public void setFreeTextSearch(String freeTextSearch)
    {
        this.freeTextSearch = freeTextSearch;
    }


    public String getFreeTextSearch()
    {
        return this.freeTextSearch;
    }


    public void setCategoryCode(String categoryCode)
    {
        this.categoryCode = categoryCode;
    }


    public String getCategoryCode()
    {
        return this.categoryCode;
    }


    public void setFilterTerms(List<SolrSearchQueryTermData> filterTerms)
    {
        this.filterTerms = filterTerms;
    }


    public List<SolrSearchQueryTermData> getFilterTerms()
    {
        return this.filterTerms;
    }


    public void setFilterQueries(List<SolrSearchFilterQueryData> filterQueries)
    {
        this.filterQueries = filterQueries;
    }


    public List<SolrSearchFilterQueryData> getFilterQueries()
    {
        return this.filterQueries;
    }


    public void setSort(String sort)
    {
        this.sort = sort;
    }


    public String getSort()
    {
        return this.sort;
    }


    public void setSearchQueryContext(SearchQueryContext searchQueryContext)
    {
        this.searchQueryContext = searchQueryContext;
    }


    public SearchQueryContext getSearchQueryContext()
    {
        return this.searchQueryContext;
    }
}
