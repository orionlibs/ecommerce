package de.hybris.platform.regioncache.key.legacy;

import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;

@Deprecated(since = "5.0", forRemoval = true)
public abstract class AbstractRegioncacheUnit implements CacheKey, CacheValueLoader
{
    protected Object typeCode;
    protected CacheKey cacheKey;


    public abstract Object[] getKeyAsArray();


    public abstract void addedToCacheBeforeComputation();


    public abstract String getTenantID();


    public final Object load(CacheKey key) throws CacheValueLoadException
    {
        try
        {
            addedToCacheBeforeComputation();
            return this;
        }
        catch(RuntimeException jaloError)
        {
            throw jaloError;
        }
        catch(Exception ex)
        {
            throw new CacheValueLoadException("Unable to load value", ex);
        }
    }


    public final CacheValueLoader getLoader()
    {
        return this;
    }


    public final CacheKey getKey()
    {
        return this;
    }


    public final String getTenantId()
    {
        return getTenantID();
    }


    public Object getTypeCode()
    {
        if(this.typeCode == null)
        {
            this.typeCode = calculateKeyCode(getKeyAsArray());
        }
        return this.typeCode;
    }


    public final CacheUnitValueType getCacheValueType()
    {
        return CacheUnitValueType.NON_SERIALIZABLE;
    }


    public String toString()
    {
        return "CacheKey(" + getTenantId() + ")[valueType=" + getCacheValueType() + ", tenantId=" + getTenantId() + ", typeCode=" +
                        LegacyCacheKeyType.getFullTypeName(this.typeCode) + "]";
    }


    public static Object calculateKeyCode(Object[] key)
    {
        return LegacyCacheKeyTypeUtil.calculateKeyCode(key);
    }
}
