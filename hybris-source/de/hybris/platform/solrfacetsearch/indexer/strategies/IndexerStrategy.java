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

public interface IndexerStrategy
{
    void setIndexOperation(IndexOperation paramIndexOperation);


    void setFacetSearchConfig(FacetSearchConfig paramFacetSearchConfig);


    void setIndexedType(IndexedType paramIndexedType);


    void setIndexedProperties(Collection<IndexedProperty> paramCollection);


    void setPks(List<PK> paramList);


    void setIndex(Index paramIndex);


    void setIndexerHints(Map<String, String> paramMap);


    void execute() throws IndexerException;
}
