package de.hybris.platform.regioncache.key.legacy;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.regioncache.key.RegistrableCacheKey;
import de.hybris.platform.regioncache.region.RegionType;

@Deprecated(since = "5.0", forRemoval = true)
public abstract class AbstractRegioncacheRegistrableUnit extends AbstractCacheUnit implements RegistrableCacheKey
{
    public AbstractRegioncacheRegistrableUnit(Cache cache)
    {
        super(cache);
    }


    public AbstractRegioncacheRegistrableUnit(Cache cache, Tenant tenant)
    {
        super(cache, tenant);
    }


    public AbstractRegioncacheRegistrableUnit(Cache cache, String tennantId)
    {
        super(cache, tennantId);
    }


    public final Object getTypeCode()
    {
        return RegionType.QUERY_CACHE_TYPE.value();
    }
}
