package de.hybris.platform.scripting.engine.internal.cache.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;

public class ModelScriptCacheKey implements CacheKey
{
    private static final String CACHED_TYPE = "__SCRIPT__";
    private final PK pk;
    private final String tenantId;


    public ModelScriptCacheKey(PK pk, String tenantId)
    {
        this.pk = pk;
        this.tenantId = tenantId;
    }


    public CacheUnitValueType getCacheValueType()
    {
        return CacheUnitValueType.NON_SERIALIZABLE;
    }


    public Object getTypeCode()
    {
        return "__SCRIPT__";
    }


    public String getTenantId()
    {
        return this.tenantId;
    }


    public PK getPk()
    {
        return this.pk;
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
        ModelScriptCacheKey that = (ModelScriptCacheKey)o;
        if(!this.pk.equals(that.pk))
        {
            return false;
        }
        if(!this.tenantId.equals(that.tenantId))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        int result = this.pk.hashCode();
        result = 31 * result + this.tenantId.hashCode();
        return result;
    }
}
