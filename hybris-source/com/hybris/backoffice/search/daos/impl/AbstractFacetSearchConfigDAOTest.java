package com.hybris.backoffice.search.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractFacetSearchConfigDAOTest
{
    static boolean isFacetSearchConfigModelCreated = false;
    @Mock
    private FlexibleSearchService flexibleSearchService;
    @InjectMocks
    private AbstractFacetSearchConfigDAOImpl abstractFacetSearchConfigDAOImpl;


    @Test
    public void shouldReturnEmptyListWhenFacetSearchConfigModelNotCreated()
    {
        Assert.assertEquals(Collections.emptyList(), this.abstractFacetSearchConfigDAOImpl.findAllSearchConfigs());
    }


    @Test
    public void shouldSearchResultWhenFacetSearchConfigModelCreated()
    {
        SearchResult searchResult = (SearchResult)Mockito.mock(SearchResult.class);
        ((FlexibleSearchService)Mockito.doReturn(searchResult).when(this.flexibleSearchService)).search((FlexibleSearchQuery)ArgumentMatchers.nullable(FlexibleSearchQuery.class));
        this.abstractFacetSearchConfigDAOImpl.setIsFacetSearchConfigModelCreated(true);
        Assert.assertEquals(searchResult.getResult(), this.abstractFacetSearchConfigDAOImpl.findAllSearchConfigs());
    }
}
