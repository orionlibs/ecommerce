package com.hybris.datahub.service;

import com.hybris.datahub.domain.RawItemStatusType;
import com.hybris.datahub.model.RawItem;
import com.hybris.datahub.runtime.domain.DataHubPool;
import com.hybris.datahub.runtime.domain.DataLoadingAction;
import java.util.List;
import java.util.Map;

public interface RawItemService
{
    List<RawItem> findInPool(DataHubPool paramDataHubPool, String paramString, int paramInt, List<DataLoadingAction> paramList, Map<Long, Long> paramMap, RawItemStatusType... paramVarArgs);


    boolean poolHasItemsWithStatus(DataHubPool paramDataHubPool, RawItemStatusType... paramVarArgs);


    void removeFromWorkingSetAvailability(List<? extends RawItem> paramList);


    void changeStatus(List<? extends RawItem> paramList, RawItemStatusType paramRawItemStatusType);


    <T extends RawItem> T clone(T paramT);


    Boolean isRawItemType(String paramString);


    <T extends RawItem> T getRawItem(long paramLong);
}
