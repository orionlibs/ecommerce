package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;

public interface FacetSearchStrategyFactory
{
    FacetSearchStrategy createStrategy(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType);
}
