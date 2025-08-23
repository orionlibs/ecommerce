package com.hybris.backoffice.search.cache.impl;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractBackofficeFacetSearchConfigCacheTest
{
    static final String typeCode = "123";
    static final PK facetSearchConfigPk = PK.createFixedCounterPK(123, 112233L);
    final Object facetSearchConfig = new Object();
    @Mock
    private ModelService modelService;
    @InjectMocks
    private AbstractBackofficeFacetSearchConfigCacheImpl abstractBackofficeFacetSearchConfigCacheImpl;


    @Before
    public void setUp() throws Exception
    {
        this.abstractBackofficeFacetSearchConfigCacheImpl.putSearchConfigForTypeCode("123", this.facetSearchConfig);
    }


    @Test
    public void shouldReturnTrueWhenCacheContainsKey()
    {
        Assert.assertTrue(this.abstractBackofficeFacetSearchConfigCacheImpl.containsSearchConfigForTypeCode("123"));
    }


    @Test
    public void shouldReturnSearchConfigForTypeCodeWhenPKExists()
    {
        ((ModelService)Mockito.doReturn(this.facetSearchConfig).when(this.modelService)).get(facetSearchConfigPk);
        Assert.assertEquals(this.facetSearchConfig, this.abstractBackofficeFacetSearchConfigCacheImpl.getSearchConfigForTypeCode("123"));
    }


    @Test
    public void shouldReturnNullConfigForTypeCodeWhenPKIsNull()
    {
        Assert.assertNull(this.abstractBackofficeFacetSearchConfigCacheImpl.getSearchConfigForTypeCode("123"));
    }


    @Test
    public void ShouldClearCacheWhenHasInvalidateCache()
    {
        this.abstractBackofficeFacetSearchConfigCacheImpl.invalidateCache();
        Assert.assertEquals(new HashMap<>(), this.abstractBackofficeFacetSearchConfigCacheImpl.cache);
    }


    @Test
    public void ShouldInvokeInvalidateCacheWhenIsOperationInvalidating()
    {
        Object[] key = new Object[3];
        key[2] = "123";
        Set<String> invalidatingTypeCodes = new HashSet<>(Arrays.asList(new String[] {"", "123"}));
        this.abstractBackofficeFacetSearchConfigCacheImpl.setInvalidatingTypecodes(invalidatingTypeCodes);
        this.abstractBackofficeFacetSearchConfigCacheImpl.keyInvalidated(key, 4, (InvalidationTarget)Mockito.mock(Cache.class),
                        (RemoteInvalidationSource)Mockito.mock(RemoteInvalidationSource.class));
        Assert.assertEquals(new HashMap<>(), this.abstractBackofficeFacetSearchConfigCacheImpl.cache);
    }
}
