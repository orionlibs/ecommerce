package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

public interface TypeValueResolver<T>
{
    void resolve(InputDocument paramInputDocument, IndexerBatchContext paramIndexerBatchContext, T paramT) throws FieldValueProviderException;
}
