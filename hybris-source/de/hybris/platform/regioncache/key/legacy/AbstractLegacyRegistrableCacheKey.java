package de.hybris.platform.regioncache.key.legacy;

import com.google.common.base.Joiner;
import de.hybris.platform.regioncache.key.RegistrableCacheKey;
import de.hybris.platform.regioncache.region.RegionType;
import de.hybris.platform.util.Utilities;
import java.util.Arrays;
import org.apache.commons.lang.ArrayUtils;

public abstract class AbstractLegacyRegistrableCacheKey<T> extends LegacyCacheKey implements RegistrableCacheKey
{
    private long[] dependentTypeGenerations;


    public AbstractLegacyRegistrableCacheKey(Object[] key, String tenantId)
    {
        super(RegionType.QUERY_CACHE_TYPE.value(), key, tenantId);
    }


    public Object getTypeCode()
    {
        return RegionType.QUERY_CACHE_TYPE.value();
    }


    public void setDependentTypeGenerations(long[] dependentTypeGeneration)
    {
        assureDependentGenerations(dependentTypeGeneration);
        this.dependentTypeGenerations = dependentTypeGeneration;
    }


    public long[] getDependentTypeGenerations()
    {
        return this.dependentTypeGenerations;
    }


    public final int hashCode()
    {
        int ret = this.cachedHash;
        if(ret == 0)
        {
            ret = this.tenantId.hashCode() ^ Arrays.deepHashCode(getLegacyKey());
            ret = ret * 31 + Arrays.hashCode(this.dependentTypeGenerations);
            this.cachedHash = ret;
        }
        return ret;
    }


    public abstract T[] getDependentTypes();


    public final boolean equals(Object object)
    {
        if(this == object)
        {
            return true;
        }
        if(object == null)
        {
            return false;
        }
        if(!(object instanceof LegacyCacheKey))
        {
            return false;
        }
        if(!getTenantId().equals(((LegacyCacheKey)object).getTenantId()))
        {
            return false;
        }
        if(!Arrays.equals(this.dependentTypeGenerations, ((AbstractLegacyRegistrableCacheKey)object).dependentTypeGenerations))
        {
            return false;
        }
        return Utilities.arrayDeepEqualsReverse(getLegacyKey(), ((LegacyCacheKey)object).getLegacyKey());
    }


    private void assureDependentGenerations(long[] dependentTypeGeneration)
    {
        if(ArrayUtils.isEmpty(dependentTypeGeneration))
        {
            throw new IllegalStateException("Provided generation for key " + getTypeCode() + ", depending on " +
                            Joiner.on(",").join(getDependentTypes()) + "  can not be empty ");
        }
    }
}
