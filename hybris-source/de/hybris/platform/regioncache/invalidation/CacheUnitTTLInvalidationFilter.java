package de.hybris.platform.regioncache.invalidation;

import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;

@Deprecated(since = "5.0", forRemoval = true)
public class CacheUnitTTLInvalidationFilter implements InvalidationFilter
{
    public boolean allowInvalidation(CacheKey key, CacheRegion region)
    {
        throw new UnsupportedOperationException();
    }
}
