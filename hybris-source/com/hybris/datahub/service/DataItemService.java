package com.hybris.datahub.service;

import com.hybris.datahub.domain.TargetItemMetadata;
import com.hybris.datahub.filter.DataItemFilter;
import com.hybris.datahub.model.BaseDataItem;
import com.hybris.datahub.model.DataItemAttribute;
import com.hybris.datahub.model.TypeAttributeDefinitions;
import com.hybris.datahub.paging.DataHubPage;
import com.hybris.datahub.paging.DataHubPageable;
import com.hybris.datahub.runtime.domain.DataHubPool;
import javax.validation.constraints.NotNull;

public interface DataItemService
{
    TypeAttributeDefinitions getItemTypeAttributes(String paramString);


    TypeAttributeDefinitions getItemTypeAttributes(String paramString1, String paramString2);


    DataItemAttribute getDataItemAttribute(String paramString1, String paramString2);


    DataItemAttribute getDataItemAttribute(String paramString, TargetItemMetadata paramTargetItemMetadata);


    DataItemAttribute getDataItemAttribute(String paramString1, String paramString2, String paramString3);


    @NotNull
    DataHubPage<? extends BaseDataItem> findDataItems(DataHubPool paramDataHubPool, String paramString, DataHubPageable paramDataHubPageable, DataItemFilter paramDataItemFilter);
}
