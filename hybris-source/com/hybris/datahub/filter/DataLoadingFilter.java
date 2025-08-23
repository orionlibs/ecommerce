package com.hybris.datahub.filter;

import com.hybris.datahub.runtime.domain.DataLoadingActionStatusType;
import javax.annotation.concurrent.Immutable;

@Immutable
public class DataLoadingFilter
{
    private final DataLoadingActionStatusType[] statuses;


    public DataLoadingFilter(DataLoadingActionStatusType[] statuses)
    {
        this.statuses = statuses;
    }


    public DataLoadingActionStatusType[] getStatuses()
    {
        return this.statuses;
    }
}
