package com.hybris.datahub.service;

import com.hybris.datahub.runtime.domain.DataHubPool;
import java.util.Map;

public interface ItemDeletionService
{
    int deleteItems(String paramString, Map<String, String> paramMap, DataHubPool paramDataHubPool);


    int deleteByIntegrationKey(String paramString1, String paramString2, DataHubPool paramDataHubPool);


    int deleteByPrimaryKey(String paramString, Map<String, ?> paramMap, DataHubPool paramDataHubPool);


    int deleteAll(String paramString, DataHubPool paramDataHubPool);
}
