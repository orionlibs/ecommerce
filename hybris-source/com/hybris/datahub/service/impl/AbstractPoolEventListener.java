package com.hybris.datahub.service.impl;

import com.hybris.datahub.runtime.domain.DataHubPool;
import com.hybris.datahub.service.DataHubFeedService;
import org.springframework.beans.factory.annotation.Required;

public class AbstractPoolEventListener
{
    private DataHubFeedService dataHubFeedService;


    protected String getPoolNameFromId(long poolId)
    {
        DataHubPool pool = getPoolById(poolId);
        return (pool != null) ? pool.getPoolName() : null;
    }


    protected DataHubPool getPoolById(long poolId)
    {
        return this.dataHubFeedService.findPoolById(Long.valueOf(poolId));
    }


    @Required
    public void setDataHubFeedService(DataHubFeedService dataHubFeedService)
    {
        this.dataHubFeedService = dataHubFeedService;
    }
}
