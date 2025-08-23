package com.hybris.datahub.runtime.domain;

import java.util.Date;
import javax.validation.constraints.NotNull;

public interface DataHubPoolAction
{
    public static final String _TYPECODE = "DataHubPoolAction";


    @NotNull
    Long getActionId();


    void setActionId(Long paramLong);


    DataHubPool getPool();


    void setPool(DataHubPool paramDataHubPool);


    Date getStartTime();


    void setStartTime(Date paramDate);


    Date getEndTime();


    void setEndTime(Date paramDate);


    Date getCreationTime();


    Date getModifiedTime();
}
