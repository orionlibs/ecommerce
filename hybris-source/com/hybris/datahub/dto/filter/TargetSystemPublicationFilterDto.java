package com.hybris.datahub.dto.filter;

import java.util.Date;
import javax.annotation.concurrent.Immutable;

@Immutable
public class TargetSystemPublicationFilterDto extends PoolActionFilterDto
{
    private final String targetSystemName;


    private TargetSystemPublicationFilterDto(String[] statuses, Date startDate, ComparisonOperator startDateOperator, Date endDate, ComparisonOperator endDateOperator, String poolName, String targetSystemName)
    {
        super(statuses, startDate, startDateOperator, endDate, endDateOperator, poolName);
        this.targetSystemName = targetSystemName;
    }


    public String getTargetSystemName()
    {
        return this.targetSystemName;
    }
}
