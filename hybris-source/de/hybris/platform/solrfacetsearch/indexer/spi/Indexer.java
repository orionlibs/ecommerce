package de.hybris.platform.solrfacetsearch.indexer.spi;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.solr.Index;
import java.util.Collection;
import org.apache.solr.common.SolrInputDocument;

public interface Indexer
{
    Collection<SolrInputDocument> indexItems(Collection<ItemModel> paramCollection, FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType) throws IndexerException, InterruptedException;


    Collection<SolrInputDocument> indexItems(Collection<ItemModel> paramCollection, FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, Collection<IndexedProperty> paramCollection1) throws IndexerException, InterruptedException;


    void removeItemsByPk(Collection<PK> paramCollection, FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, Index paramIndex) throws IndexerException, InterruptedException;
}
