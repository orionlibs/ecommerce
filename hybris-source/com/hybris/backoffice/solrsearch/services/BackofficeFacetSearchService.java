package com.hybris.backoffice.solrsearch.services;

import com.hybris.backoffice.solrsearch.dataaccess.BackofficeSearchQuery;
import com.hybris.cockpitng.search.data.SearchQueryData;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;

public interface BackofficeFacetSearchService extends FacetSearchService
{
    BackofficeSearchQuery createBackofficeSolrSearchQuery(SearchQueryData paramSearchQueryData);
}
