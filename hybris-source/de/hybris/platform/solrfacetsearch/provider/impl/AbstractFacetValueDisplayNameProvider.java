package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FacetDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

public abstract class AbstractFacetValueDisplayNameProvider implements FacetValueDisplayNameProvider, FacetDisplayNameProvider
{
    public final String getDisplayName(SearchQuery query, String name)
    {
        throw new IllegalStateException("Do not call the FacetDisplayNameProvider#getDisplayName method call the FacetValueDisplayNameProvider#getDisplayName method instead.");
    }


    public abstract String getDisplayName(SearchQuery paramSearchQuery, IndexedProperty paramIndexedProperty, String paramString);
}
