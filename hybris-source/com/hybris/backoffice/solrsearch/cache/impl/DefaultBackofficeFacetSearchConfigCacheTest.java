package com.hybris.backoffice.solrsearch.cache.impl;

import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBackofficeFacetSearchConfigCacheTest
{
    private static final String KEY_WITH_EXISTING_VALUE = "existingValue";
    private static final String KEY_WITH_NULL_VALUE = "nullValue";
    private static final String KEY_WITHOUT_VALUE = "noValue";
    private static final PK PK_1 = PK.fromLong(1L);
    @InjectMocks
    private DefaultBackofficeFacetSearchConfigCache cache;
    @Mock
    private ModelService modelService;
    @Mock
    private BackofficeIndexedTypeToSolrFacetSearchConfigModel facetSearchConfig;


    @Before
    public void setUp()
    {
        Mockito.when(this.facetSearchConfig.getPk()).thenReturn(PK_1);
        Mockito.when(this.modelService.get((PK)Matchers.eq(PK_1))).thenReturn(this.facetSearchConfig);
        this.cache.putSearchConfigForTypeCode("existingValue", this.facetSearchConfig);
        this.cache.putSearchConfigForTypeCode("nullValue", null);
    }


    @Test
    public void testExistingValue()
    {
        Assert.assertTrue(this.cache.containsSearchConfigForTypeCode("existingValue"));
        Assert.assertEquals(this.facetSearchConfig, this.cache.getSearchConfigForTypeCode("existingValue"));
    }


    @Test
    public void testNullValue()
    {
        Assert.assertTrue(this.cache.containsSearchConfigForTypeCode("nullValue"));
        Assert.assertNull(this.cache.getSearchConfigForTypeCode("nullValue"));
    }


    @Test
    public void testNoValue()
    {
        Assert.assertFalse(this.cache.containsSearchConfigForTypeCode("noValue"));
        Assert.assertNull(this.cache.getSearchConfigForTypeCode("noValue"));
    }


    @Test
    public void testInvalidation()
    {
        testExistingValue();
        this.cache.invalidateCache();
        Assert.assertFalse(this.cache.containsSearchConfigForTypeCode("existingValue"));
    }


    @Test
    public void testConcurrency() throws InterruptedException
    {
        ReadingThread readingThread1 = new ReadingThread(this, 10 + (new Random()).nextInt(10));
        ReadingThread readingThread2 = new ReadingThread(this, 10 + (new Random()).nextInt(10));
        ReadingThread readingThread3 = new ReadingThread(this, 10 + (new Random()).nextInt(10));
        ReadingAndWritingThread readingAndWritingThread = new ReadingAndWritingThread(this, 30);
        readingThread1.run();
        readingThread2.run();
        readingThread3.run();
        readingAndWritingThread.run();
        readingThread1.join();
        readingThread2.join();
        readingThread3.join();
        readingAndWritingThread.join();
        Assert.assertFalse(this.cache.containsSearchConfigForTypeCode("existingValue"));
        Assert.assertFalse(this.cache.containsSearchConfigForTypeCode("nullValue"));
        Assert.assertFalse(this.cache.containsSearchConfigForTypeCode("noValue"));
    }
}
