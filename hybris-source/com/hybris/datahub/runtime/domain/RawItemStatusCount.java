package com.hybris.datahub.runtime.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public interface RawItemStatusCount
{
    public static final String _TYPECODE = "RawItemStatusCount";


    @NotNull
    DataHubPool getPool();


    void setPool(DataHubPool paramDataHubPool);


    @Min(0L)
    long getIgnoredCount();


    void setIgnoredCount(long paramLong);


    @Min(0L)
    long getPendingCount();


    void setPendingCount(long paramLong);


    @Min(0L)
    long getProcessedCount();


    void setProcessedCount(long paramLong);
}
