package com.hybris.datahub.runtime.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public interface CanonicalPublicationStatusCount
{
    public static final String _TYPECODE = "CanonicalPublicationStatusCount";


    @NotNull
    DataHubPool getPool();


    void setPool(DataHubPool paramDataHubPool);


    @Min(0L)
    long getSuccessCount();


    void setSuccessCount(long paramLong);


    @Min(0L)
    long getInternalErrorCount();


    void setInternalErrorCount(long paramLong);


    @Min(0L)
    long getExternalErrorCount();


    void setExternalErrorCount(long paramLong);


    @Min(0L)
    long getIgnoredCount();


    void setIgnoredCount(long paramLong);
}
