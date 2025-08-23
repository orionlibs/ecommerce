package de.hybris.platform.solrfacetsearch.indexer;

import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;

public interface SolrIndexedTypeCodeResolver
{
    String resolveIndexedTypeCode(SolrIndexedTypeModel paramSolrIndexedTypeModel);
}
