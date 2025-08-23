package com.hybris.backoffice.searchservices.populators;

import com.hybris.backoffice.search.daos.ItemModelSearchDAO;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.search.data.SnSearchHit;
import de.hybris.platform.searchservices.search.data.SnSearchResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchResultItemModelsPopulatorTest
{
    @Mock
    private ItemModelSearchDAO itemModelSearchDAO;
    @InjectMocks
    private SearchResultItemModelsPopulator searchResultItemModelsPopulator;
    private final SnSearchResultSourceData snSearchResultSourceData = (SnSearchResultSourceData)Mockito.mock(SnSearchResultSourceData.class);
    private final SnSearchResultConverterData snSearchResultConverterData = (SnSearchResultConverterData)Mockito.mock(SnSearchResultConverterData.class);
    private static final String TYPE_CODE = "typeCode";
    private static final String HIT_PK = "123456";


    @Test
    public void shouldSetItemModelsAsNullWhenGotNoSearchHits()
    {
        SnSearchResult snSearchResult = (SnSearchResult)Mockito.mock(SnSearchResult.class);
        Mockito.when(this.snSearchResultSourceData.getSnSearchResult()).thenReturn(snSearchResult);
        Mockito.when(snSearchResult.getSearchHits()).thenReturn(new ArrayList());
        this.searchResultItemModelsPopulator.populate(this.snSearchResultSourceData, this.snSearchResultConverterData);
        ((SnSearchResultConverterData)Mockito.verify(this.snSearchResultConverterData)).setItemModels(Collections.emptyList());
    }


    @Test
    public void shouldSetItemModelsWhenGotSearchHits()
    {
        SnSearchResult snSearchResult = (SnSearchResult)Mockito.mock(SnSearchResult.class);
        SnSearchHit searchHit = (SnSearchHit)Mockito.mock(SnSearchHit.class);
        List<SnSearchHit> searchHits = Arrays.asList(new SnSearchHit[] {searchHit});
        List<Long> resultPKs = Arrays.asList(new Long[] {Long.valueOf("123456")});
        ItemModel itemModel = (ItemModel)Mockito.mock(ItemModel.class);
        List<ItemModel> itemModels = Arrays.asList(new ItemModel[] {itemModel});
        Mockito.when(searchHit.getId()).thenReturn("123456");
        Mockito.when(this.snSearchResultSourceData.getSnSearchResult()).thenReturn(snSearchResult);
        Mockito.when(snSearchResult.getSearchHits()).thenReturn(searchHits);
        Mockito.when(this.snSearchResultSourceData.getTypeCode()).thenReturn("typeCode");
        Mockito.when(this.itemModelSearchDAO.findAll("typeCode", resultPKs)).thenReturn(itemModels);
        this.searchResultItemModelsPopulator.populate(this.snSearchResultSourceData, this.snSearchResultConverterData);
        ((SnSearchResultConverterData)Mockito.verify(this.snSearchResultConverterData)).setItemModels(itemModels);
    }
}
