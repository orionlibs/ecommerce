package de.hybris.platform.regioncache;

import de.hybris.platform.regioncache.key.RegistrableCacheKey;
import java.util.Collection;

@Deprecated(since = "5.0", forRemoval = true)
public interface CacheRegistry
{
    void register(RegistrableCacheKey paramRegistrableCacheKey);


    Collection<RegistrableCacheKey> invalidate(Object paramObject);


    void evict(RegistrableCacheKey paramRegistrableCacheKey);


    void clear();
}
