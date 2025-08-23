package com.hybris.datahub.runtime.domain;

import javax.validation.constraints.NotNull;

public interface CompositionAction extends DataHubPoolAction
{
    public static final String _TYPECODE = "CompositionAction";


    @NotNull
    CompositionActionStatusType getStatus();


    void setStatus(CompositionActionStatusType paramCompositionActionStatusType);


    Long getCount();


    void setCount(Long paramLong);
}
