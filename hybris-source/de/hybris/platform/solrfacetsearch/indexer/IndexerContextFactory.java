package de.hybris.platform.solrfacetsearch.indexer;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import java.util.Collection;

public interface IndexerContextFactory<T extends IndexerContext>
{
    T createContext(long paramLong, IndexOperation paramIndexOperation, boolean paramBoolean, FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, Collection<IndexedProperty> paramCollection);


    void prepareContext() throws IndexerException;


    void initializeContext() throws IndexerException;


    T getContext();


    void destroyContext() throws IndexerException;


    void destroyContext(Exception paramException);
}
