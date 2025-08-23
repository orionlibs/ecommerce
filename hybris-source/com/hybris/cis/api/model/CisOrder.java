package com.hybris.cis.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisOrder implements Identifiable
{
    @XmlAttribute(name = "id")
    private String id;
    @XmlElement(name = "date")
    private Date date;
    @XmlElement(name = "currency")
    private String currency;
    @XmlElementWrapper(name = "addresses")
    @XmlElement(name = "address")
    private List<CisAddress> addresses = new ArrayList<>();
    @XmlElementWrapper(name = "lineItems")
    @XmlElement(name = "lineItem")
    private List<CisLineItem> lineItems;
    @XmlElement(name = "vendorParameters")
    private AnnotationHashMap vendorParameters;


    public String getId()
    {
        return this.id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public Date getDate()
    {
        return (this.date == null) ? null : new Date(this.date.getTime());
    }


    public void setDate(Date date)
    {
        this.date = date;
    }


    public List<CisAddress> getAddresses()
    {
        return this.addresses;
    }


    public void setAddresses(List<CisAddress> shipments)
    {
        this.addresses = shipments;
    }


    public List<CisLineItem> getLineItems()
    {
        return this.lineItems;
    }


    public void setLineItems(List<CisLineItem> lineItems)
    {
        this.lineItems = lineItems;
    }


    public String getCurrency()
    {
        return this.currency;
    }


    public void setCurrency(String currency)
    {
        this.currency = currency;
    }


    public CisAddress getAddressByType(CisAddressType type)
    {
        for(CisAddress cisAddress : getAddresses())
        {
            if(type.equals(cisAddress.getType()))
            {
                return cisAddress;
            }
        }
        return null;
    }


    public String toString()
    {
        StringBuilder value = new StringBuilder();
        value.append("CisOrder [id=").append(getId()).append("]");
        return value.toString();
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
