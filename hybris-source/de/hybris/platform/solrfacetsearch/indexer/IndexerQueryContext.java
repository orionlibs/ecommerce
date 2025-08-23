package de.hybris.platform.solrfacetsearch.indexer;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import java.util.List;
import java.util.Map;

public interface IndexerQueryContext
{
    FacetSearchConfig getFacetSearchConfig();


    IndexedType getIndexedType();


    String getQuery();


    Map<String, Object> getQueryParameters();


    Status getStatus();


    List<Exception> getFailureExceptions();


    Map<String, Object> getAttributes();
}
