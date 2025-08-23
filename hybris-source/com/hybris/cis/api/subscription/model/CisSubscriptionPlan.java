package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.AnnotationHashMap;
import com.hybris.cis.api.validation.XSSSafe;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionPlan
{
    @XmlAttribute(name = "id")
    @XSSSafe
    private String id;
    @XmlElement(name = "name")
    @XSSSafe
    private String name;
    @XmlElementWrapper(name = "charges")
    @XmlElement(name = "charge")
    @Valid
    private List<CisChargeEntry> charges = new ArrayList<>();
    @XmlElementWrapper(name = "usageCharges")
    @XmlElement(name = "usageCharge")
    @Valid
    private List<CisUsageCharge> usageCharges = new ArrayList<>();
    @XmlElement(name = "vendorParameters")
    @Valid
    private AnnotationHashMap vendorParameters;


    public String getId()
    {
        return this.id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public List<CisChargeEntry> getCharges()
    {
        return this.charges;
    }


    public void setCharges(List<CisChargeEntry> charges)
    {
        this.charges = charges;
    }


    public List<CisUsageCharge> getUsageCharges()
    {
        return this.usageCharges;
    }


    public void setUsageCharges(List<CisUsageCharge> usageCharges)
    {
        this.usageCharges = usageCharges;
    }


    public AnnotationHashMap getVendorParameters()
    {
        return this.vendorParameters;
    }


    public void setVendorParameters(AnnotationHashMap vendorParameters)
    {
        this.vendorParameters = vendorParameters;
    }
}
