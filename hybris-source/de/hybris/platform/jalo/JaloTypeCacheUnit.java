package de.hybris.platform.jalo;

import de.hybris.platform.cache.AbstractCacheUnit;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.regioncache.key.RegistrableCacheKey;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.typesystem.PlatformStringUtils;

public abstract class JaloTypeCacheUnit extends AbstractCacheUnit
{
    private final JaloTypeCacheKey cacheKey;
    private final Object[] jaloKey;


    protected JaloTypeCacheUnit(Cache cache, int typecode, Object additionalKey, int invalidationType)
    {
        super(cache);
        this
                        .jaloKey = new Object[] {Cache.CACHEKEY_JALOTYPE, Integer.toString(invalidationType), PlatformStringUtils.valueOf(typecode), additionalKey};
        this.cacheKey = new JaloTypeCacheKey(PlatformStringUtils.valueOf(typecode), this.jaloKey, cache.getTenantId());
    }


    protected JaloTypeCacheUnit(Cache cache, int typecode, Object additionalKey)
    {
        this(cache, typecode, additionalKey, 1);
    }


    public Object[] createKey()
    {
        return this.jaloKey;
    }


    public RegistrableCacheKey getKey()
    {
        return (RegistrableCacheKey)this.cacheKey;
    }


    public Object getCached()
    {
        try
        {
            return Utilities.getCacheBoundVersion(get());
        }
        catch(Exception e)
        {
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new JaloSystemException(e);
        }
    }


    public abstract Object compute();
}
