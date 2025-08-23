package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.validation.XSSSafe;
import java.util.Date;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name = "subscriptionCreateRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionCreateRequest extends CisSubscriptionRequest
{
    @XmlElement(name = "orderId")
    @XSSSafe
    private String orderId;
    @XmlElement(name = "currency")
    @XSSSafe
    private String currency;
    @XmlElement(name = "orderDate")
    @XmlSchemaType(name = "date")
    private Date orderDate;
    @XmlElement(name = "merchantAccountId")
    @XSSSafe
    private String merchantAccountId;
    @XmlElement(name = "merchantPaymentMethodId")
    @XSSSafe
    private String merchantPaymentMethodId;
    @XmlElement(name = "subscriptionItem")
    @Valid
    private CisSubscriptionItem subscriptionItem;


    public String getOrderId()
    {
        return this.orderId;
    }


    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }


    public String getMerchantAccountId()
    {
        return this.merchantAccountId;
    }


    public void setMerchantAccountId(String merchantAccountId)
    {
        this.merchantAccountId = merchantAccountId;
    }


    public CisSubscriptionItem getSubscriptionItem()
    {
        return this.subscriptionItem;
    }


    public void setSubscriptionItem(CisSubscriptionItem subscriptionItem)
    {
        this.subscriptionItem = subscriptionItem;
    }


    public String getCurrency()
    {
        return this.currency;
    }


    public void setCurrency(String currency)
    {
        this.currency = currency;
    }


    public Date getOrderDate()
    {
        return (this.orderDate == null) ? null : new Date(this.orderDate.getTime());
    }


    public void setOrderDate(Date orderDate)
    {
        this.orderDate = orderDate;
    }


    public String getMerchantPaymentMethodId()
    {
        return this.merchantPaymentMethodId;
    }


    public void setMerchantPaymentMethodId(String merchantPaymentMethodId)
    {
        this.merchantPaymentMethodId = merchantPaymentMethodId;
    }
}
