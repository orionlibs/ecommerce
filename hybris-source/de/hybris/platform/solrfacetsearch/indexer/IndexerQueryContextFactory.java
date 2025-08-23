package de.hybris.platform.solrfacetsearch.indexer;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import java.util.Map;

public interface IndexerQueryContextFactory<T extends IndexerQueryContext>
{
    T createContext(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, String paramString, Map<String, Object> paramMap) throws IndexerException;


    void initializeContext() throws IndexerException;


    T getContext();


    void destroyContext() throws IndexerException;


    void destroyContext(Exception paramException);
}
