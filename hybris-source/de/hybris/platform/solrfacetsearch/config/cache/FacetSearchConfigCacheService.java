package de.hybris.platform.solrfacetsearch.config.cache;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;

public interface FacetSearchConfigCacheService
{
    FacetSearchConfig putOrGetFromCache(String paramString);


    void invalidate(String paramString);
}
