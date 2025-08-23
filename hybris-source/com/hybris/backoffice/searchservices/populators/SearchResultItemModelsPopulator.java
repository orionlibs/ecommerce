package com.hybris.backoffice.searchservices.populators;

import com.hybris.backoffice.search.daos.ItemModelSearchDAO;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.search.data.SnSearchHit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchResultItemModelsPopulator implements Populator<SnSearchResultSourceData, SnSearchResultConverterData>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultItemModelsPopulator.class);
    private ItemModelSearchDAO itemModelSearchDAO;


    public void populate(SnSearchResultSourceData snSearchResultSourceData, SnSearchResultConverterData snSearchResultConverterData)
    {
        List<Long> resultPKs = (List<Long>)snSearchResultSourceData.getSnSearchResult().getSearchHits().stream().filter(Objects::nonNull).map(this::getPKFromSearchResult).collect(Collectors.toList());
        List<ItemModel> itemModels = new ArrayList<>();
        if(resultPKs.isEmpty())
        {
            snSearchResultConverterData.setItemModels(Collections.emptyList());
        }
        else
        {
            itemModels = getItemModelSearchDAO().findAll(snSearchResultSourceData.getTypeCode(), resultPKs);
            snSearchResultConverterData.setItemModels(itemModels);
        }
        if(resultPKs.size() != itemModels.size())
        {
            LOGGER.warn("Cloud Searchservices query returned {} pks, flexibleSearch found {} items. Probable cause is that Cloud Searchservices has documents in the index for items which have been removed from the platform or some restrictions are applied on products search",
                            Integer.valueOf(resultPKs.size()), Integer.valueOf(itemModels.size()));
        }
    }


    public ItemModelSearchDAO getItemModelSearchDAO()
    {
        return this.itemModelSearchDAO;
    }


    public void setItemModelSearchDAO(ItemModelSearchDAO itemModelSearchDAO)
    {
        this.itemModelSearchDAO = itemModelSearchDAO;
    }


    private Long getPKFromSearchResult(SnSearchHit searchHit)
    {
        return Long.valueOf(searchHit.getId());
    }
}
