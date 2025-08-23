package com.hybris.backoffice.solrsearch.cache;

import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;

@Deprecated(since = "2105", forRemoval = true)
public interface BackofficeFacetSearchConfigCache
{
    boolean containsSearchConfigForTypeCode(String paramString);


    BackofficeIndexedTypeToSolrFacetSearchConfigModel getSearchConfigForTypeCode(String paramString);


    void putSearchConfigForTypeCode(String paramString, BackofficeIndexedTypeToSolrFacetSearchConfigModel paramBackofficeIndexedTypeToSolrFacetSearchConfigModel);


    void invalidateCache();
}
