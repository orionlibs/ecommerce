package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class SearchQueryConverterData
{
    private FacetSearchContext facetSearchContext;
    private SearchQuery searchQuery;
    private Map<String, Object> attributes = new HashMap<>();


    public FacetSearchContext getFacetSearchContext()
    {
        return this.facetSearchContext;
    }


    public void setFacetSearchContext(FacetSearchContext facetSearchContext)
    {
        this.facetSearchContext = facetSearchContext;
    }


    public SearchQuery getSearchQuery()
    {
        return this.searchQuery;
    }


    public void setSearchQuery(SearchQuery searchQuery)
    {
        this.searchQuery = searchQuery;
    }


    public Map<String, Object> getAttributes()
    {
        return this.attributes;
    }


    public void setAttributes(Map<String, Object> attributes)
    {
        this.attributes = attributes;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        SearchQueryConverterData that = (SearchQueryConverterData)obj;
        return (new EqualsBuilder())
                        .append(this.facetSearchContext, that.facetSearchContext)
                        .append(this.searchQuery, that.searchQuery)
                        .append(this.attributes, that.attributes)
                        .isEquals();
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.facetSearchContext, this.searchQuery, this.attributes});
    }
}
