package com.hybris.datahub.filter;

import com.google.common.base.Preconditions;
import com.hybris.datahub.dto.filter.ComparisonOperator;
import com.hybris.datahub.runtime.domain.CompositionActionStatusType;
import com.hybris.datahub.runtime.domain.DataHubPool;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import javax.annotation.concurrent.Immutable;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Immutable
public class CompositionActionFilter
{
    private final CompositionActionStatusType[] statuses;
    private final Date startDate;
    private final ComparisonOperator startDateOperator;
    private final Date endDate;
    private final ComparisonOperator endDateOperator;
    private final DataHubPool pool;


    public CompositionActionFilter()
    {
        this.statuses = null;
        this.startDate = null;
        this.startDateOperator = null;
        this.endDate = null;
        this.endDateOperator = null;
        this.pool = null;
    }


    public CompositionActionFilter(CompositionActionStatusType... statuses)
    {
        this.statuses = statuses;
        this.startDate = null;
        this.startDateOperator = null;
        this.endDate = null;
        this.endDateOperator = null;
        this.pool = null;
    }


    public CompositionActionFilter(CompositionActionStatusType[] statuses, Date startDate, ComparisonOperator startDateOperator, Date endDate, ComparisonOperator endDateOperator, DataHubPool pool)
    {
        Preconditions.checkArgument((((startDate != null) ? true : false) == ((startDateOperator != null) ? true : false)), "startDate and startDateOperator must both be populated or null");
        Preconditions.checkArgument((((endDate != null) ? true : false) == ((endDateOperator != null) ? true : false)), "endDate and endDateOperator must both be populated or null");
        this.statuses = statuses;
        this.startDate = startDate;
        this.startDateOperator = startDateOperator;
        this.endDate = endDate;
        this.endDateOperator = endDateOperator;
        this.pool = pool;
    }


    public CompositionActionStatusType[] getStatuses()
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


    public int hashCode()
    {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(17, 35);
        hashCodeBuilder.append((this.statuses != null) ? new HashSet(Arrays.asList((Object[])this.statuses)) : null)
                        .append(this.startDate)
                        .append(this.startDateOperator)
                        .append(this.endDate)
                        .append(this.endDateOperator)
                        .append(this.pool);
        return hashCodeBuilder.hashCode();
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        CompositionActionFilter that = (CompositionActionFilter)o;
        return (hashCode() == that.hashCode());
    }
}
