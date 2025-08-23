package de.hybris.platform.regioncache.key.legacy;

import de.hybris.platform.regioncache.key.AbstractCacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;
import de.hybris.platform.util.Utilities;
import java.util.Arrays;

public class LegacyCacheKey extends AbstractCacheKey
{
    protected final Object[] key;


    public LegacyCacheKey(Object[] key, String tenantId)
    {
        super(CacheUnitValueType.NON_SERIALIZABLE, LegacyCacheKeyTypeUtil.calculateKeyCode(key), tenantId);
        this.key = key;
        if(key != null && key.length > 0 && key[0] != null && key[0].getClass().isArray())
        {
            throw new IllegalArgumentException("LegacyCacheKey cannot handle multi-keys. Use RegistrableCacheKey instead.");
        }
    }


    public LegacyCacheKey(String typeCode, Object[] key, String tenantId)
    {
        super(CacheUnitValueType.NON_SERIALIZABLE, typeCode, tenantId);
        this.key = key;
    }


    public Object[] getLegacyKey()
    {
        return this.key;
    }


    public String toString()
    {
        return "LegacyCacheKey(" + this.tenantId + ")[key=" + Arrays.toString(this.key) + ", valueType=" + this.valueType + ", typeCode=" +
                        LegacyCacheKeyType.getFullTypeName(this.typeCode) + "}";
    }


    public int hashCode()
    {
        int ret = this.cachedHash;
        if(ret == 0)
        {
            ret = this.tenantId.hashCode() ^ Arrays.deepHashCode(getLegacyKey());
            this.cachedHash = ret;
        }
        return ret;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(!(obj instanceof LegacyCacheKey))
        {
            return false;
        }
        LegacyCacheKey other = (LegacyCacheKey)obj;
        if(!this.tenantId.equals(other.tenantId))
        {
            return false;
        }
        if(!Utilities.arrayDeepEqualsReverse(this.key, other.key))
        {
            return false;
        }
        return true;
    }
}
