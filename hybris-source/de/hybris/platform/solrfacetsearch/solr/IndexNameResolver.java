package de.hybris.platform.solrfacetsearch.solr;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;

public interface IndexNameResolver
{
    String resolve(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, String paramString);
}
