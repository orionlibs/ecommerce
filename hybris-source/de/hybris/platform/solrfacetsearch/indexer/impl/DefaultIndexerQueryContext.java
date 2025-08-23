package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueryContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultIndexerQueryContext implements IndexerQueryContext
{
    private final Map<String, Object> attributes = new HashMap<>();
    private final List<Exception> failureExceptions = new ArrayList<>();
    private IndexerQueryContext.Status status;
    private Map<String, Object> queryParameters;
    private String query;
    private IndexedType indexedType;
    private FacetSearchConfig facetSearchConfig;


    public FacetSearchConfig getFacetSearchConfig()
    {
        return this.facetSearchConfig;
    }


    public void setFacetSearchConfig(FacetSearchConfig facetSearchConfig)
    {
        this.facetSearchConfig = facetSearchConfig;
    }


    public IndexedType getIndexedType()
    {
        return this.indexedType;
    }


    public void setIndexedType(IndexedType indexedType)
    {
        this.indexedType = indexedType;
    }


    public String getQuery()
    {
        return this.query;
    }


    public void setQuery(String query)
    {
        this.query = query;
    }


    public Map<String, Object> getQueryParameters()
    {
        return this.queryParameters;
    }


    public void setQueryParameters(Map<String, Object> queryParameters)
    {
        this.queryParameters = queryParameters;
    }


    public IndexerQueryContext.Status getStatus()
    {
        return this.status;
    }


    public void setStatus(IndexerQueryContext.Status status)
    {
        this.status = status;
    }


    public void addFailureException(Exception exception)
    {
        this.failureExceptions.add(exception);
    }


    public List<Exception> getFailureExceptions()
    {
        return Collections.unmodifiableList(this.failureExceptions);
    }


    public Map<String, Object> getAttributes()
    {
        return this.attributes;
    }
}
