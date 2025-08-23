package com.hybris.datahub.runtime.domain;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;

public interface DataHubAction extends Serializable
{
    public static final String _TYPECODE = "DataHubAction";


    @NotNull
    Long getActionId();


    void setActionId(Long paramLong);


    Date getStartTime();


    void setStartTime(Date paramDate);


    Date getEndTime();


    void setEndTime(Date paramDate);
}
