package com.hybris.backoffice.search.cache;

public interface BackofficeFacetSearchConfigCache<T>
{
    boolean containsSearchConfigForTypeCode(String paramString);


    T getSearchConfigForTypeCode(String paramString);


    void putSearchConfigForTypeCode(String paramString, T paramT);


    void invalidateCache();
}
