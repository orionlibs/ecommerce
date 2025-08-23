package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import java.util.Collection;

public interface ValueResolver<T extends de.hybris.platform.core.model.ItemModel>
{
    void resolve(InputDocument paramInputDocument, IndexerBatchContext paramIndexerBatchContext, Collection<IndexedProperty> paramCollection, T paramT) throws FieldValueProviderException;
}
