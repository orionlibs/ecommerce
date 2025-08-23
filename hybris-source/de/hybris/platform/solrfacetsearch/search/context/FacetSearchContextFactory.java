package de.hybris.platform.solrfacetsearch.search.context;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

public interface FacetSearchContextFactory<T extends FacetSearchContext>
{
    T createContext(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, SearchQuery paramSearchQuery);


    void initializeContext() throws FacetSearchException;


    T getContext();


    void destroyContext() throws FacetSearchException;


    void destroyContext(Exception paramException);
}
