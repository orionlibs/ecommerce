package de.hybris.platform.regioncache.key;

import de.hybris.platform.regioncache.key.legacy.LegacyCacheKeyType;

public abstract class AbstractCacheKey implements CacheKey
{
    protected final CacheUnitValueType valueType;
    protected final String tenantId;
    protected int cachedHash = 0;
    protected final Object typeCode;


    public AbstractCacheKey(Object typeCode, String tenantId)
    {
        this.valueType = CacheUnitValueType.NON_SERIALIZABLE;
        this.typeCode = typeCode;
        this.tenantId = tenantId;
    }


    public AbstractCacheKey(CacheUnitValueType valueType, Object typeCode, String tenantId)
    {
        this.valueType = valueType;
        this.typeCode = typeCode;
        this.tenantId = tenantId;
    }


    public CacheUnitValueType getCacheValueType()
    {
        return this.valueType;
    }


    public Object getTypeCode()
    {
        return this.typeCode;
    }


    public int hashCode()
    {
        if(this.cachedHash == 0)
        {
            int prime = 31;
            int result = 1;
            result = 31 * result + ((this.typeCode == null) ? 0 : this.typeCode.hashCode());
            this.cachedHash = this.tenantId.hashCode() ^ result;
        }
        return this.cachedHash;
    }


    public String getTenantId()
    {
        return this.tenantId;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        AbstractCacheKey other = (AbstractCacheKey)obj;
        if(!this.tenantId.equals(other.tenantId))
        {
            return false;
        }
        if(this.typeCode == null)
        {
            if(other.typeCode != null)
            {
                return false;
            }
        }
        else if(!this.typeCode.equals(other.typeCode))
        {
            return false;
        }
        return true;
    }


    public String toString()
    {
        return "CacheKey(" + this.tenantId + ")[valueType=" + this.valueType + ", tenantId=" + this.tenantId + ", typeCode=" +
                        LegacyCacheKeyType.getFullTypeName(this.typeCode) + "]";
    }
}
