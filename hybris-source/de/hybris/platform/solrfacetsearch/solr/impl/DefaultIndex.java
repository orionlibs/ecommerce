package de.hybris.platform.solrfacetsearch.solr.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.solr.Index;

public class DefaultIndex implements Index
{
    private static final long serialVersionUID = 1L;
    private String name;
    private FacetSearchConfig facetSearchConfig;
    private IndexedType indexedType;
    private String qualifier;


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


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


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }
}
