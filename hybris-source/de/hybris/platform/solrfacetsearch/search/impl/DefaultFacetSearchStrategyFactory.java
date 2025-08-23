package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.FacetSearchStrategy;
import de.hybris.platform.solrfacetsearch.search.FacetSearchStrategyFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFacetSearchStrategyFactory implements FacetSearchStrategyFactory
{
    private FacetSearchStrategy legacyFacetSearchStrategy;
    private FacetSearchStrategy defaultFacetSearchStrategy;


    public FacetSearchStrategy createStrategy(FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        if(facetSearchConfig.getSearchConfig().isLegacyMode())
        {
            return this.legacyFacetSearchStrategy;
        }
        return this.defaultFacetSearchStrategy;
    }


    public FacetSearchStrategy getDefaultFacetSearchStrategy()
    {
        return this.defaultFacetSearchStrategy;
    }


    public void setDefaultFacetSearchStrategy(FacetSearchStrategy defaultFacetSearchStrategy)
    {
        this.defaultFacetSearchStrategy = defaultFacetSearchStrategy;
    }


    public FacetSearchStrategy getLegacyFacetSearchStrategy()
    {
        return this.legacyFacetSearchStrategy;
    }


    @Required
    public void setLegacyFacetSearchStrategy(FacetSearchStrategy legacyFacetSearchStrategy)
    {
        this.legacyFacetSearchStrategy = legacyFacetSearchStrategy;
    }
}
