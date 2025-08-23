package de.hybris.platform.regioncache.key;

public interface CacheKey
{
    CacheUnitValueType getCacheValueType();


    Object getTypeCode();


    String getTenantId();
}
