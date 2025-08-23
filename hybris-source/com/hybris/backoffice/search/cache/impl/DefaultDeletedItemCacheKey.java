package com.hybris.backoffice.search.cache.impl;

import de.hybris.platform.regioncache.key.AbstractCacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;
import org.apache.commons.lang.StringUtils;

public class DefaultDeletedItemCacheKey extends AbstractCacheKey
{
    private String cacheKey;


    public DefaultDeletedItemCacheKey(Object typeCode, String cacheKey, String tenantId)
    {
        super(CacheUnitValueType.SERIALIZABLE, typeCode, tenantId);
        this.cacheKey = cacheKey;
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
        if(!super.equals(o))
        {
            return false;
        }
        DefaultDeletedItemCacheKey that = (DefaultDeletedItemCacheKey)o;
        if(StringUtils.isEmpty(this.cacheKey) && StringUtils.isEmpty(that.cacheKey))
        {
            return true;
        }
        if(!StringUtils.isEmpty(this.cacheKey) &&
                        !StringUtils.isEmpty(that.cacheKey))
        {
            return this.cacheKey.equals(that.cacheKey);
        }
        return false;
    }


    public int hashCode()
    {
        if(this.cachedHash == 0)
        {
            int prime = 31;
            int result = super.hashCode();
            this.cachedHash = 31 * result + (StringUtils.isEmpty(this.cacheKey) ? 0 : this.cacheKey.hashCode());
        }
        return this.cachedHash;
    }


    public String getCacheKey()
    {
        return this.cacheKey;
    }
}
