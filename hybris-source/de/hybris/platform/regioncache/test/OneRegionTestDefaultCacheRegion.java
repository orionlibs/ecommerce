package de.hybris.platform.regioncache.test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.region.CacheRegionNotSpecifiedException;
import de.hybris.platform.test.RunnerCreator;
import de.hybris.platform.test.TestThreadsHolder;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test/OneRegionTestDefaultCacheRegion-context.xml"}, inheritLocations = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OneRegionTestDefaultCacheRegion extends AbstractCacheControllerOneRegionTest
{
    @Test
    public void concurrentValueLoadTest() throws CacheRegionNotSpecifiedException, CacheValueLoadException
    {
        Assert.assertNotNull(this.controller);
        AtomicLong loadCounter = new AtomicLong();
        String[] keyNames = {"A", "B", "C", "D", "E"};
        TestThreadsHolder<Runnable> randomAccessHolder = new TestThreadsHolder(keyNames.length * 7, (RunnerCreator)new Object(this, keyNames, loadCounter));
        randomAccessHolder.startAll();
        Assert.assertTrue(randomAccessHolder.waitForAll(60L, TimeUnit.SECONDS));
        Assert.assertEquals(keyNames.length, loadCounter.longValue());
        CacheStatistics stats = this.region.getCacheRegionStatistics();
        Assert.assertNotNull(stats);
        Assert.assertEquals(keyNames.length, stats.getMisses());
        Assert.assertEquals((keyNames.length * 7 * 1000 - keyNames.length), stats.getHits());
        Assert.assertEquals(0L, stats.getEvictions());
        Assert.assertEquals(0L, stats.getInvalidations());
        Object type = (new TestCacheKey("")).getTypeCode();
        Assert.assertEquals(keyNames.length, stats.getMisses(type));
        Assert.assertEquals((keyNames.length * 7 * 1000 - keyNames.length), stats.getHits(type));
        Assert.assertEquals(0L, stats.getEvictions(type));
    }
}
