package de.hybris.platform.regioncache;

import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;
import de.hybris.platform.regioncache.test.TestRegistrableCacheKey;
import java.util.Collection;
import org.junit.Assert;

public final class CacheTestHelper
{
    public static void registryEvictionTest(DefaultCacheController controller, CacheRegion region, String[] typeCodes) throws CacheRegionNotSpecifiedException, CacheValueLoadException
    {
        Assert.assertNotNull(controller);
        int length = (int)region.getCacheMaxEntries() + 10;
        Object object = new Object();
        CacheKey[] askedKeys = new CacheKey[length];
        for(int i = 0; i < length; i++)
        {
            TestRegistrableCacheKey testRegistrableCacheKey = new TestRegistrableCacheKey(String.valueOf(i), new String[] {typeCodes[i % typeCodes.length]});
            askedKeys[i] = (CacheKey)testRegistrableCacheKey;
            CacheRegion tmpRegion = resolveCacheRegion((CacheKey)testRegistrableCacheKey, false, controller);
            Assert.assertNotNull(tmpRegion);
            Assert.assertEquals(region.getName(), tmpRegion.getName());
            controller.getWithLoader((CacheKey)testRegistrableCacheKey, (CacheValueLoader)object);
        }
        CacheStatistics stats = region.getCacheRegionStatistics();
        Assert.assertNotNull(stats);
        Assert.assertEquals(length, stats.getMisses());
        Assert.assertEquals(0L, stats.getHits());
        Assert.assertEquals(10L, stats.getEvictions());
        Assert.assertEquals(0L, stats.getInvalidations());
    }


    public static CacheRegion resolveCacheRegion(CacheKey key, boolean forInvalidation, DefaultCacheController controller)
    {
        if(forInvalidation)
        {
            Collection<CacheRegion> regions = controller.resolveRegionsForInvalidation(key);
            if(regions == null)
            {
                return null;
            }
            if(regions.isEmpty())
            {
                return null;
            }
            if(regions.size() > 1)
            {
                throw new RuntimeException("More than one region resolved");
            }
            return regions.iterator().next();
        }
        return controller.resolveCacheRegionForAdd(key);
    }


    public static CacheRegion resolveCacheRegion(CacheKey key, DefaultCacheController controller)
    {
        return resolveCacheRegion(key, false, controller);
    }


    public static CacheRegion resolveCacheRegion(DefaultCacheController controller, CacheKey key)
    {
        return resolveCacheRegion(key, false, controller);
    }
}
