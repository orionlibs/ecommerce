package com.hybris.datahub.service;

import com.hybris.datahub.filter.DataLoadingFilter;
import com.hybris.datahub.paging.DataHubPage;
import com.hybris.datahub.paging.DataHubPageable;
import com.hybris.datahub.runtime.domain.DataHubFeed;
import com.hybris.datahub.runtime.domain.DataLoadingAction;
import javax.validation.constraints.NotNull;

public interface DataLoadingService
{
    DataLoadingAction createFailedAction(DataHubFeed paramDataHubFeed, Long paramLong, String paramString);


    @NotNull
    DataHubPage<DataLoadingAction> findAll(DataHubPageable paramDataHubPageable, DataLoadingFilter paramDataLoadingFilter);
}
