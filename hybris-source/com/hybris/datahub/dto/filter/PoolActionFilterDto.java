package com.hybris.datahub.dto.filter;

import com.google.common.base.Preconditions;
import java.util.Date;

public abstract class PoolActionFilterDto
{
    private final String[] statuses;
    private final Date startDate;
    private final ComparisonOperator startDateOperator;
    private final Date endDate;
    private final ComparisonOperator endDateOperator;
    private final String poolName;


    PoolActionFilterDto(String[] statuses, Date startDate, ComparisonOperator startDateOperator, Date endDate, ComparisonOperator endDateOperator, String poolName)
    {
        Preconditions.checkArgument((((startDate != null) ? true : false) == ((startDateOperator != null) ? true : false)), "startDate and startDateOperator must both be populated or null");
        Preconditions.checkArgument((((endDate != null) ? true : false) == ((endDateOperator != null) ? true : false)), "endDate and endDateOperator must both be populated or null");
        this.statuses = statuses;
        this.startDate = startDate;
        this.startDateOperator = startDateOperator;
        this.endDate = endDate;
        this.endDateOperator = endDateOperator;
        this.poolName = poolName;
    }


    public String[] getStatuses()
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


    public String getPoolName()
    {
        return this.poolName;
    }
}
