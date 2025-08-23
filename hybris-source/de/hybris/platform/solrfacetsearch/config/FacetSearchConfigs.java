package de.hybris.platform.solrfacetsearch.config;

public final class FacetSearchConfigs
{
    public static FacetSearchConfig createFacetSearchConfig(String name, String description, IndexConfig indexConfig, SearchConfig searchConfig, SolrConfig solrConfig)
    {
        FacetSearchConfig facetSearchConfig = new FacetSearchConfig();
        facetSearchConfig.setName(name);
        facetSearchConfig.setDescription(description);
        facetSearchConfig.setIndexConfig(indexConfig);
        facetSearchConfig.setSearchConfig(searchConfig);
        facetSearchConfig.setSolrConfig(solrConfig);
        return facetSearchConfig;
    }
}
