package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;

public class FacetSearchConfig implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private IndexConfig indexConfig;
    private SearchConfig searchConfig;
    private SolrConfig solrConfig;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setIndexConfig(IndexConfig indexConfig)
    {
        this.indexConfig = indexConfig;
    }


    public IndexConfig getIndexConfig()
    {
        return this.indexConfig;
    }


    public void setSearchConfig(SearchConfig searchConfig)
    {
        this.searchConfig = searchConfig;
    }


    public SearchConfig getSearchConfig()
    {
        return this.searchConfig;
    }


    public void setSolrConfig(SolrConfig solrConfig)
    {
        this.solrConfig = solrConfig;
    }


    public SolrConfig getSolrConfig()
    {
        return this.solrConfig;
    }
}
