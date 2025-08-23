package com.hybris.datahub.dto.count;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbstractItemStatusCountData
{
    protected String poolName;


    public String getPoolName()
    {
        return this.poolName;
    }


    public void setPoolName(String poolName)
    {
        this.poolName = poolName;
    }
}
