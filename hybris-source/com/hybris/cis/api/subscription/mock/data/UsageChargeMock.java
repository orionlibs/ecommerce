package com.hybris.cis.api.subscription.mock.data;

import java.io.Serializable;
import java.util.List;

public class UsageChargeMock implements Serializable
{
    private static final long serialVersionUID = -6767737529939942551L;
    private String name;
    private String type;
    private String unitId;
    private List<UsageChargeTierMock> tiers;


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getUnitId()
    {
        return this.unitId;
    }


    public void setUnitId(String unitId)
    {
        this.unitId = unitId;
    }


    public List<UsageChargeTierMock> getTiers()
    {
        return this.tiers;
    }


    public void setTiers(List<UsageChargeTierMock> tiers)
    {
        this.tiers = tiers;
    }
}
