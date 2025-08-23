package de.hybris.platform.regioncache.region.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.regioncache.CacheConfiguration;
import de.hybris.platform.regioncache.generation.GenerationalCounterService;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.RegistrableCacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CleanUpQueryCacheRegionStrategy;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class DefaultCleanUpQueryCacheRegionStrategy implements CleanUpQueryCacheRegionStrategy, InitializingBean
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCleanUpQueryCacheRegionStrategy.class);
    private static final String REGION_NAME = "queryCacheRegion";
    private final CacheConfiguration cacheConfiguration;
    private final GenerationalCounterService<String> generationalCounterService;
    private Tenant myTenant;


    public DefaultCleanUpQueryCacheRegionStrategy(CacheConfiguration cacheConfiguration, GenerationalCounterService<String> generationalCounterService)
    {
        Objects.requireNonNull(cacheConfiguration, "cacheConfiguration should be not null");
        Objects.requireNonNull(generationalCounterService, "generationalCounterService should be not null");
        this.cacheConfiguration = cacheConfiguration;
        this.generationalCounterService = generationalCounterService;
    }


    public void cleanUp()
    {
        Map<String, Long> generations = this.generationalCounterService.getGenerations(this.myTenant.getTenantID());
        removeFromCacheRegion(generations, getQueryCacheRegion());
    }


    private void removeFromCacheRegion(Map<String, Long> generations, CacheRegion cr)
    {
        int counter = 0;
        Collection<CacheKey> allKeys = cr.getAllKeys();
        for(CacheKey key : allKeys)
        {
            if(!(key instanceof RegistrableCacheKey))
            {
                continue;
            }
            RegistrableCacheKey registrableCacheKey = (RegistrableCacheKey)key;
            Object[] dependentTypes = registrableCacheKey.getDependentTypes();
            long[] dependentTypesGenerations = registrableCacheKey.getDependentTypeGenerations();
            int i = 0;
            for(Object dependentType : dependentTypes)
            {
                String dependentTypeString = dependentType.toString();
                Long generation = generations.getOrDefault(dependentTypeString, Long.valueOf(0L));
                if(dependentTypesGenerations[i] < generation.longValue())
                {
                    cr.remove(key, true);
                    counter++;
                    break;
                }
                i++;
            }
        }
        logStatistics(counter, allKeys.size());
    }


    private void logStatistics(int removed, int orginalSize)
    {
        LOG.debug("{} size: {}", "queryCacheRegion", Integer.valueOf(orginalSize));
        LOG.debug("number of cached keys removed: {}", Integer.valueOf(removed));
    }


    private CacheRegion getQueryCacheRegion()
    {
        return (CacheRegion)this.cacheConfiguration.getRegions()
                        .stream()
                        .filter(region -> region.getName().equals("queryCacheRegion"))
                        .findFirst().orElseThrow(NullPointerException::new);
    }


    public void afterPropertiesSet() throws Exception
    {
        this.myTenant = Registry.getCurrentTenantNoFallback();
    }
}
