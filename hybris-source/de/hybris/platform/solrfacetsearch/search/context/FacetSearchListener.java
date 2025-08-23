package de.hybris.platform.solrfacetsearch.search.context;

import de.hybris.platform.solrfacetsearch.search.FacetSearchException;

public interface FacetSearchListener
{
    void beforeSearch(FacetSearchContext paramFacetSearchContext) throws FacetSearchException;


    void afterSearch(FacetSearchContext paramFacetSearchContext) throws FacetSearchException;


    void afterSearchError(FacetSearchContext paramFacetSearchContext) throws FacetSearchException;
}
