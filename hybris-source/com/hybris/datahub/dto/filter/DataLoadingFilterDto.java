package com.hybris.datahub.dto.filter;

import javax.annotation.concurrent.Immutable;

@Immutable
public class DataLoadingFilterDto
{
    private final String[] statuses;


    private DataLoadingFilterDto(String[] statuses)
    {
        this.statuses = statuses;
    }


    public String[] getStatuses()
    {
        return this.statuses;
    }
}
