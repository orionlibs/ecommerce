package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.AnnotationHashMap;
import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.model.CisLineItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subscriptionorder")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionOrder
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
    @XmlElement(name = "merchantAccountId")
    private String merchantAccountId;
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<CisSubscriptionItem> items = new ArrayList<>();


    public void setItems(List<CisSubscriptionItem> items)
    {
        this.items = items;
    }


    public List<CisSubscriptionItem> getItems()
    {
        return this.items;
    }


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


    public String getCurrency()
    {
        return this.currency;
    }


    public void setCurrency(String currency)
    {
        this.currency = currency;
    }


    public List<CisAddress> getAddresses()
    {
        return this.addresses;
    }


    public void setAddresses(List<CisAddress> addresses)
    {
        this.addresses = addresses;
    }


    public List<CisLineItem> getLineItems()
    {
        return this.lineItems;
    }


    public void setLineItems(List<CisLineItem> lineItems)
    {
        this.lineItems = lineItems;
    }


    public AnnotationHashMap getVendorParameters()
    {
        return this.vendorParameters;
    }


    public void setVendorParameters(AnnotationHashMap vendorParameters)
    {
        this.vendorParameters = vendorParameters;
    }


    public String getMerchantAccountId()
    {
        return this.merchantAccountId;
    }


    public void setMerchantAccountId(String merchantAccountId)
    {
        this.merchantAccountId = merchantAccountId;
    }
}
