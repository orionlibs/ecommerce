package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.validation.XSSSafe;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CisUsageCharge
{
    @XmlElement(name = "name")
    @XSSSafe
    private String name;
    @XmlElement(name = "type")
    @XSSSafe
    private String type;
    @XmlElement(name = "unitId")
    @XSSSafe
    private String unitId;
    @XmlElement(name = "tier")
    private List<CisUsageChargeTier> tiers;


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


    public List<CisUsageChargeTier> getTiers()
    {
        return this.tiers;
    }


    public void setTiers(List<CisUsageChargeTier> tiers)
    {
        this.tiers = tiers;
    }
}
