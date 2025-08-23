package com.hybris.datahub.client;

import com.hybris.datahub.dto.filter.DataItemFilterDto;

public class LuceneQueryStringFactory implements DataItemQueryStringFactory
{
    private static final String STATUS_PARAMETER = "status";


    public String createFrom(DataItemFilterDto filter)
    {
        return (filter != null) ? createFromNonNullFilter(filter) : "";
    }


    private static String createFromNonNullFilter(DataItemFilterDto filter)
    {
        assert filter.getStatuses() != null : "Statuses are never null";
        return QueryBuilder.query()
                        .withStatuses(filter.getStatuses())
                        .withKeyFields(filter.getAttributeValues())
                        .build();
    }
}
