package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import java.util.Map;

public interface FacetSearchService
{
    SearchQuery createSearchQuery(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType);


    SearchQuery createPopulatedSearchQuery(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType);


    SearchQuery createFreeTextSearchQuery(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, String paramString);


    SearchQuery createSearchQueryFromTemplate(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, String paramString);


    SearchQuery createFreeTextSearchQueryFromTemplate(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, String paramString1, String paramString2);


    SearchResult search(SearchQuery paramSearchQuery) throws FacetSearchException;


    SearchResult search(SearchQuery paramSearchQuery, Map<String, String> paramMap) throws FacetSearchException;
}
