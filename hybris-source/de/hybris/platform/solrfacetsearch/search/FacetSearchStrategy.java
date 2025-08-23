package de.hybris.platform.solrfacetsearch.search;

import java.util.Map;

public interface FacetSearchStrategy
{
    SearchResult search(SearchQuery paramSearchQuery, Map<String, String> paramMap) throws FacetSearchException;
}
