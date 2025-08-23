package de.hybris.platform.regioncache.test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.regioncache.CacheLifecycleCallback;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.DefaultCacheController;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test/OneRegionTestEHCacheRegion-context.xml"}, inheritLocations = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OneRegionTestEHCacheRegion extends AbstractCacheControllerOneRegionTest
{
    @Test
    public void resolveCacheRegionForAdd()
    {
        int keys = 7000;
        DefaultCacheController ctrl = this.controller;
        CacheKey[] keyTbl = new CacheKey[7000];
        for(int i = 0; i < 7000; i++)
        {
            keyTbl[i] = (CacheKey)new TestCacheKey(Integer.valueOf(i), CacheUnitValueType.SERIALIZABLE, "key");
        }
        long start = System.currentTimeMillis();
        for(int j = 0; j < 7000; j++)
        {
            ctrl.resolveCacheRegionForAdd(keyTbl[j]);
        }
        long end = System.currentTimeMillis();
        LOGGER.info("7000 resolvings took [ms] " + end - start);
    }


    @Ignore("Proves that EHCache region FIFO is not strict")
    @Test
    public void ehCacheFifoTest()
    {
        this.region.clearCache();
        Object object = new Object(this);
        this.region.registerLifecycleCallback((CacheLifecycleCallback)object);
        int size = (int)this.region.getCacheMaxEntries();
        TestCacheValueLoader testCacheValueLoader = new TestCacheValueLoader();
        TestCacheKey key = new TestCacheKey(Integer.valueOf(0), CacheUnitValueType.SERIALIZABLE, Integer.toString(0));
        this.region.getWithLoader((CacheKey)key, (CacheValueLoader)testCacheValueLoader);
        sleep(2);
        for(int i = 1; i <= size; i++)
        {
            this.region.getWithLoader((CacheKey)new TestCacheKey(Integer.valueOf(i), CacheUnitValueType.SERIALIZABLE, Integer.toString(i % 7)), (CacheValueLoader)testCacheValueLoader);
            sleep(2);
        }
        Assert.assertFalse("First element put is not evicted !", this.region.containsKey((CacheKey)key));
    }


    private void sleep(int i)
    {
        try
        {
            Thread.sleep(i);
        }
        catch(InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
