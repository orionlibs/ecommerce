package com.hybris.datahub.dto.filter;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.concurrent.Immutable;

@Immutable
public class DataItemFilterDto
{
    private final String[] statuses;
    private final Map<String, Object> attributeValues;


    private DataItemFilterDto(String[] statuses)
    {
        this.statuses = (statuses != null) ? statuses : new String[0];
        this.attributeValues = new HashMap<>();
    }


    public String[] getStatuses()
    {
        return this.statuses;
    }


    private DataItemFilterDto withAttributeValues(Map<String, ?> values)
    {
        if(values != null)
        {
            this.attributeValues.putAll(values);
        }
        return this;
    }


    public Map<String, Object> getAttributeValues()
    {
        return this.attributeValues;
    }


    public static Builder dataItemFilterBuilder()
    {
        return new Builder();
    }
}
