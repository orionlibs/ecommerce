package com.hybris.datahub.dto.filter;

import java.util.Date;

public class CompositionFilterDto extends PoolActionFilterDto
{
    private CompositionFilterDto(String[] statuses, Date startDate, ComparisonOperator startDateOperator, Date endDate, ComparisonOperator endDateOperator, String poolName)
    {
        super(statuses, startDate, startDateOperator, endDate, endDateOperator, poolName);
    }
}
