package de.hybris.platform.regioncache.key;

import com.google.common.base.Joiner;
import de.hybris.platform.regioncache.key.legacy.LegacyCacheKeyType;
import de.hybris.platform.regioncache.region.RegionType;
import java.util.Arrays;
import org.apache.commons.lang.ArrayUtils;

public abstract class AbstractRegistrableCacheKey extends AbstractCacheKey implements RegistrableCacheKey<String>
{
    private long[] dependentTypeGenerations;
    private final String[] dependentTypes;


    public AbstractRegistrableCacheKey(String tenantId, String[] dependentTypes)
    {
        super(RegionType.QUERY_CACHE_TYPE.value(), tenantId);
        this.dependentTypes = dependentTypes;
    }


    public Object getTypeCode()
    {
        return RegionType.QUERY_CACHE_TYPE.value();
    }


    public String toString()
    {
        return "RegistrableCacheKey [valueType=" + this.valueType + ", tenantId=" + this.tenantId + ", typeCode=" +
                        LegacyCacheKeyType.getFullTypeName(this.typeCode) + ", dependent=" + Arrays.deepToString((Object[])getDependentTypes()) + "{" +
                        LegacyCacheKeyType.getFullTypeName(getDependentTypes()[0]) + "}]";
    }


    public void setDependentTypeGenerations(long[] dependentTypeGeneration)
    {
        assureDependentGenerations(dependentTypeGeneration);
        if(ArrayUtils.isEmpty(getDependentTypeGenerations()))
        {
            this.dependentTypeGenerations = dependentTypeGeneration;
        }
    }


    public long[] getDependentTypeGenerations()
    {
        return this.dependentTypeGenerations;
    }


    public String[] getDependentTypes()
    {
        return this.dependentTypes;
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
