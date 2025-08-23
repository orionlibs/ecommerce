/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cache.impl;

import de.hybris.platform.regioncache.key.AbstractCacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;

public class ObjectCacheKey extends AbstractCacheKey
{
    private final Object objectKey;


    public ObjectCacheKey(final Object typeCode, final Object objectKey, final String tenantId)
    {
        super(CacheUnitValueType.SERIALIZABLE, typeCode, tenantId);
        this.objectKey = objectKey;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!super.equals(o))
        {
            return false;
        }
        final ObjectCacheKey other = (ObjectCacheKey)o;
        if(objectKey == null)
        {
            if(other.objectKey != null)
            {
                return false;
            }
        }
        else if(!objectKey.equals(other.objectKey))
        {
            return false;
        }
        return true;
    }


    @Override
    public int hashCode()
    {
        if(cachedHash == 0)
        {
            final int prime = 31;
            final int result = super.hashCode();
            this.cachedHash = prime * result + ((objectKey == null) ? 0 : objectKey.hashCode());
        }
        return this.cachedHash;
    }


    @Override
    public String toString()
    {
        return "ObjectLabelCacheKey [objectKey=" + objectKey + ", valueType=" + valueType + ", tenantId=" + tenantId
                        + ", cachedHash=" + cachedHash + ", typeCode=" + typeCode + "]";
    }


    public Object getObjectKey()
    {
        return objectKey;
    }
}
