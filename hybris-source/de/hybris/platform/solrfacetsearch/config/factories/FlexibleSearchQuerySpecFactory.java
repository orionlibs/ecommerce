package de.hybris.platform.solrfacetsearch.config.factories;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FlexibleSearchQuerySpec;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeFlexibleSearchQuery;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

public interface FlexibleSearchQuerySpecFactory
{
    FlexibleSearchQuerySpec createIndexQuery(IndexedTypeFlexibleSearchQuery paramIndexedTypeFlexibleSearchQuery, IndexedType paramIndexedType, FacetSearchConfig paramFacetSearchConfig) throws SolrServiceException;
}
