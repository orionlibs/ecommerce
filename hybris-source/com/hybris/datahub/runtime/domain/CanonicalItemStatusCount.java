package com.hybris.datahub.runtime.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public interface CanonicalItemStatusCount
{
    public static final String _TYPECODE = "CanonicalItemStatusCount";


    @NotNull
    DataHubPool getPool();


    void setPool(DataHubPool paramDataHubPool);


    @Min(0L)
    long getSuccessCount();


    void setSuccessCount(long paramLong);


    @Min(0L)
    long getArchivedCount();


    void setArchivedCount(long paramLong);


    @Min(0L)
    long getErrorCount();


    void setErrorCount(long paramLong);


    @Min(0L)
    long getDeletedCount();


    void setDeletedCount(long paramLong);
}
