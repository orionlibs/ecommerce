package com.hybris.datahub.filter;

import com.google.common.base.Preconditions;
import com.hybris.datahub.domain.TargetSystem;
import com.hybris.datahub.dto.filter.ComparisonOperator;
import com.hybris.datahub.runtime.domain.DataHubPool;
import com.hybris.datahub.runtime.domain.PublicationActionStatusType;
import java.util.Date;
import javax.annotation.concurrent.Immutable;

@Immutable
public class TargetSystemPublicationFilter
{
    private final PublicationActionStatusType[] statuses;
    private final Date startDate;
    private final ComparisonOperator startDateOperator;
    private final Date endDate;
    private final ComparisonOperator endDateOperator;
    private final DataHubPool pool;
    private final TargetSystem targetSystem;


    public TargetSystemPublicationFilter()
    {
        this.statuses = null;
        this.startDate = null;
        this.startDateOperator = null;
        this.endDate = null;
        this.endDateOperator = null;
        this.pool = null;
        this.targetSystem = null;
    }


    public TargetSystemPublicationFilter(PublicationActionStatusType[] statuses, Date startDate, ComparisonOperator startDateOperator, Date endDate, ComparisonOperator endDateOperator, DataHubPool pool, TargetSystem targetSystem)
    {
        Preconditions.checkArgument((((startDate != null) ? true : false) == ((startDateOperator != null) ? true : false)), "startDate and startDateOperator must both be populated or null");
        Preconditions.checkArgument((((endDate != null) ? true : false) == ((endDateOperator != null) ? true : false)), "endDate and endDateOperator must both be populated or null");
        this.statuses = statuses;
        this.startDate = startDate;
        this.startDateOperator = startDateOperator;
        this.endDate = endDate;
        this.endDateOperator = endDateOperator;
        this.pool = pool;
        this.targetSystem = targetSystem;
    }


    public PublicationActionStatusType[] getStatuses()
    {
        return this.statuses;
    }


    public Date getStartDate()
    {
        return this.startDate;
    }


    public ComparisonOperator getStartDateOperator()
    {
        return this.startDateOperator;
    }


    public Date getEndDate()
    {
        return this.endDate;
    }


    public ComparisonOperator getEndDateOperator()
    {
        return this.endDateOperator;
    }


    public DataHubPool getPool()
    {
        return this.pool;
    }


    public TargetSystem getTargetSystem()
    {
        return this.targetSystem;
    }
}
