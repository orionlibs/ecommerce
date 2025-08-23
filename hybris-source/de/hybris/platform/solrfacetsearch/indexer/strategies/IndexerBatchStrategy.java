package de.hybris.platform.solrfacetsearch.indexer.strategies;

import de.hybris.platform.core.PK;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.solr.Index;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IndexerBatchStrategy
{
    void setExternalIndexOperation(boolean paramBoolean);


    void setFacetSearchConfig(FacetSearchConfig paramFacetSearchConfig);


    void setIndex(Index paramIndex);


    void setIndexedProperties(Collection<IndexedProperty> paramCollection);


    void setIndexedType(IndexedType paramIndexedType);


    void setIndexerHints(Map<String, String> paramMap);


    void setIndexOperation(IndexOperation paramIndexOperation);


    void setIndexOperationId(long paramLong);


    void setPks(List<PK> paramList);


    void execute() throws InterruptedException, IndexerException;
}
