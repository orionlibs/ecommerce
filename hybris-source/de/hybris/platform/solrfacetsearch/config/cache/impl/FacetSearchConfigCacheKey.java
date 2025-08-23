package de.hybris.platform.solrfacetsearch.config.cache.impl;

import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;

public class FacetSearchConfigCacheKey implements CacheKey
{
    private static final String CACHED_TYPE = "__FACET_SEARCH_CONFIG__";
    private final String configName;
    private final String language;
    private final String tenantId;


    public FacetSearchConfigCacheKey(String configName, String language, String tenantId)
    {
        this.configName = configName;
        this.language = language;
        this.tenantId = tenantId;
    }


    public CacheUnitValueType getCacheValueType()
    {
        return CacheUnitValueType.NON_SERIALIZABLE;
    }


    public Object getTypeCode()
    {
        return "__FACET_SEARCH_CONFIG__";
    }


    public String getTenantId()
    {
        return this.tenantId;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        FacetSearchConfigCacheKey that = (FacetSearchConfigCacheKey)o;
        if(!this.configName.equals(that.configName))
        {
            return false;
        }
        if(!this.language.equals(that.language))
        {
            return false;
        }
        return this.tenantId.equals(that.tenantId);
    }


    public int hashCode()
    {
        int result = this.configName.hashCode();
        result = 31 * result + this.language.hashCode();
        result = 31 * result + this.tenantId.hashCode();
        return result;
    }


    public String toString()
    {
        return "FacetSearchConfigCacheKey[ configName = " + this.configName + " , language=" + this.language + " , tenantId=" + this.tenantId + " ]";
    }


    public String getName()
    {
        return this.configName;
    }


    public String getLanguage()
    {
        return this.language;
    }
}
