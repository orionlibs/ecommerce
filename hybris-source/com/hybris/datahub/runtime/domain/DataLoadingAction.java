package com.hybris.datahub.runtime.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface DataLoadingAction extends DataHubFeedAction
{
    public static final String _TYPECODE = "DataLoadingAction";


    @NotNull
    DataLoadingActionStatusType getStatus();


    void setStatus(DataLoadingActionStatusType paramDataLoadingActionStatusType);


    Long getCount();


    void setCount(Long paramLong);


    @Size(max = 255)
    String getMessage();


    void setMessage(String paramString);
}
