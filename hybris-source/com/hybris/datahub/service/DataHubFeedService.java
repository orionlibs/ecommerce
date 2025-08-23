package com.hybris.datahub.service;

import com.hybris.datahub.paging.DataHubPage;
import com.hybris.datahub.paging.DataHubPageable;
import com.hybris.datahub.pooling.PoolWorkingSet;
import com.hybris.datahub.pooling.PoolingException;
import com.hybris.datahub.runtime.domain.CompositionAction;
import com.hybris.datahub.runtime.domain.DataHubFeed;
import com.hybris.datahub.runtime.domain.DataHubPool;
import java.util.List;
import javax.validation.constraints.NotNull;

public interface DataHubFeedService
{
    public static final String DEFAULT_FEED = "DEFAULT_FEED";


    List<DataHubFeed> getDataFeeds();


    DataHubFeed findDataFeed(long paramLong);


    DataHubFeed findDataFeedByName(String paramString);


    List<DataHubPool> getDataPools();


    List<DataHubPool> getDeletablePools();


    @NotNull
    DataHubPage<DataHubPool> getDataPools(DataHubPageable paramDataHubPageable);


    DataHubPool findPoolById(Long paramLong);


    DataHubPool findPoolByFeedName(String paramString);


    DataHubPool findPoolByName(String paramString);


    DataHubPool createPool(String paramString, DataHubFeed paramDataHubFeed);


    DataHubFeed createFeed(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6);


    DataHubPool findCurrentPoolForFeed(DataHubFeed paramDataHubFeed) throws PoolingException;


    PoolWorkingSet findWorkingSetForComposition(DataHubPool paramDataHubPool, CompositionAction paramCompositionAction);


    PoolWorkingSet findWorkingSetForPublication(DataHubPool paramDataHubPool);


    PoolWorkingSet findWorkingSetForRepublication(DataHubPool paramDataHubPool);
}
