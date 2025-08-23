package de.hybris.platform.commercefacades.search.data;

import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import java.io.Serializable;
import java.util.List;

public class SearchQueryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String value;
    private List<SearchFilterQueryData> filterQueries;
    private SearchQueryContext searchQueryContext;


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }


    public void setFilterQueries(List<SearchFilterQueryData> filterQueries)
    {
        this.filterQueries = filterQueries;
    }


    public List<SearchFilterQueryData> getFilterQueries()
    {
        return this.filterQueries;
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
