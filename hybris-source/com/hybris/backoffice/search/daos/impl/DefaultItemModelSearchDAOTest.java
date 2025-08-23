package com.hybris.backoffice.search.daos.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultItemModelSearchDAOTest
{
    @Mock
    private FlexibleSearchService flexibleSearchService;
    @InjectMocks
    @Spy
    private DefaultItemModelSearchDAO defaultItemModelSearchDAO;


    @Test
    public void shouldReturnNewArrayListWhenItemsIsEmpty()
    {
        String typeCode = "123";
        Object object = new Object(this);
        ((FlexibleSearchService)Mockito.doReturn(Mockito.mock(SearchResult.class)).when(this.flexibleSearchService)).search((FlexibleSearchQuery)Matchers.any(FlexibleSearchQuery.class));
        Assert.assertEquals(Lists.newArrayList(), this.defaultItemModelSearchDAO.findAll("123", (List)object));
    }


    @Test
    public void shouldReturnItemsWhenItemsIsEmpty()
    {
        String typeCode = "123";
        Object object1 = new Object(this);
        SearchResult searchResult = (SearchResult)Mockito.mock(SearchResult.class);
        Object object2 = new Object(this);
        ((FlexibleSearchService)Mockito.doReturn(searchResult).when(this.flexibleSearchService)).search((FlexibleSearchQuery)Matchers.any(FlexibleSearchQuery.class));
        ((SearchResult)Mockito.doReturn(object2).when(searchResult)).getResult();
        this.defaultItemModelSearchDAO.findAll("123", (List)object1);
        ((DefaultItemModelSearchDAO)Mockito.verify(this.defaultItemModelSearchDAO)).orderItemsByPkList((List)Matchers.any(), (List)Matchers.any());
    }
}
