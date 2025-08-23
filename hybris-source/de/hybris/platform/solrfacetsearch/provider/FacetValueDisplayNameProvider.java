package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

public interface FacetValueDisplayNameProvider
{
    String getDisplayName(SearchQuery paramSearchQuery, IndexedProperty paramIndexedProperty, String paramString);
}
