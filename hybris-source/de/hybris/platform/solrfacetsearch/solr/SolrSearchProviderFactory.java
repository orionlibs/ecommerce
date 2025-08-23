package de.hybris.platform.solrfacetsearch.solr;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

public interface SolrSearchProviderFactory
{
    SolrSearchProvider getSearchProvider(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType) throws SolrServiceException;
}
