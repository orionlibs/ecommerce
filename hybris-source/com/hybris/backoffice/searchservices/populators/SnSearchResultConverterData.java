package com.hybris.backoffice.searchservices.populators;

import com.hybris.cockpitng.search.data.FullTextSearchData;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;

public class SnSearchResultConverterData
{
    private List<ItemModel> itemModels;
    private FullTextSearchData fullTextSearchData;


    public List<ItemModel> getItemModels()
    {
        return this.itemModels;
    }


    public void setItemModels(List<ItemModel> itemModels)
    {
        this.itemModels = itemModels;
    }


    public FullTextSearchData getFullTextSearchData()
    {
        return this.fullTextSearchData;
    }


    public void setFullTextSearchData(FullTextSearchData fullTextSearchData)
    {
        this.fullTextSearchData = fullTextSearchData;
    }
}
