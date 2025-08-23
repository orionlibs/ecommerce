package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.solr.client.solrj.response.QueryResponse;

public class SearchResultConverterData
{
    private FacetSearchContext facetSearchContext;
    private QueryResponse queryResponse;
    private final Map<String, Object> attributes = new HashMap<>();


    public FacetSearchContext getFacetSearchContext()
    {
        return this.facetSearchContext;
    }


    public void setFacetSearchContext(FacetSearchContext facetSearchContext)
    {
        this.facetSearchContext = facetSearchContext;
    }


    public QueryResponse getQueryResponse()
    {
        return this.queryResponse;
    }


    public void setQueryResponse(QueryResponse queryResponse)
    {
        this.queryResponse = queryResponse;
    }


    public Map<String, Object> getAttributes()
    {
        return this.attributes;
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
        SearchResultConverterData that = (SearchResultConverterData)obj;
        return (new EqualsBuilder())
                        .append(this.facetSearchContext, that.facetSearchContext)
                        .append(this.queryResponse, that.queryResponse)
                        .append(this.attributes, that.attributes)
                        .isEquals();
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.facetSearchContext, this.queryResponse, this.attributes});
    }
}
