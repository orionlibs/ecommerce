package de.hybris.platform.solrfacetsearch.indexer;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.solr.Index;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IndexerBatchContext
{
    long getIndexOperationId();


    IndexOperation getIndexOperation();


    boolean isExternalIndexOperation();


    FacetSearchConfig getFacetSearchConfig();


    IndexedType getIndexedType();


    Collection<IndexedProperty> getIndexedProperties();


    List<PK> getPks();


    void setPks(List<PK> paramList);


    List<ItemModel> getItems();


    void setItems(List<ItemModel> paramList);


    Index getIndex();


    void setIndex(Index paramIndex);


    Map<String, String> getIndexerHints();


    Map<String, Object> getAttributes();


    List<InputDocument> getInputDocuments();


    Status getStatus();


    List<Exception> getFailureExceptions();
}
