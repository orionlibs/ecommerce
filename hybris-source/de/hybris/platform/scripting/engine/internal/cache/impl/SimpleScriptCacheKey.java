package de.hybris.platform.scripting.engine.internal.cache.impl;

import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;

public class SimpleScriptCacheKey implements CacheKey
{
    private static final String CACHED_TYPE = "__SCRIPT__";
    private final String protocol;
    private final String path;
    private final String tenantId;


    public SimpleScriptCacheKey(String protocol, String path, String tenantId)
    {
        this.protocol = protocol;
        this.path = path;
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
        SimpleScriptCacheKey that = (SimpleScriptCacheKey)o;
        if(!this.path.equals(that.path))
        {
            return false;
        }
        if(!this.protocol.equals(that.protocol))
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
        int result = this.protocol.hashCode();
        result = 31 * result + this.path.hashCode();
        result = 31 * result + this.tenantId.hashCode();
        return result;
    }


    public String getProtocol()
    {
        return this.protocol;
    }


    public String getPath()
    {
        return this.path;
    }
}
