package de.hybris.platform.scripting.engine.internal.cache.impl;

import de.hybris.platform.regioncache.CacheController;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.scripting.engine.internal.cache.ScriptExecutablesCacheService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultScriptExecutableCacheService implements ScriptExecutablesCacheService
{
    private static final Logger LOG = Logger.getLogger(DefaultScriptExecutableCacheService.class);
    private CacheController cacheController;


    public ScriptExecutablesCacheService.ScriptDTO putOrGetFromCache(CacheKey cacheKey, CacheValueLoader loader)
    {
        return (ScriptExecutablesCacheService.ScriptDTO)this.cacheController.getWithLoader(cacheKey, loader);
    }


    public void invalidate(CacheKey cacheKey)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Invalidating cache for key: " + cacheKey);
        }
        this.cacheController.invalidate(cacheKey);
    }


    @Required
    public void setCacheController(CacheController cacheController)
    {
        this.cacheController = cacheController;
    }
}
