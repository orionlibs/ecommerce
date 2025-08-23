package com.hybris.cis.api.subscription.model;

import com.hybris.cis.api.model.CisResult;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name = "subscriptionData")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisSubscriptionData extends CisResult
{
    @XmlElement(name = "merchantAccountId")
    private String merchantAccountId;
    @XmlElement(name = "currency")
    private String currency;
    @XmlElement(name = "subscriptionId")
    private String subscriptionId;
    @XmlElement(name = "subscriptionName")
    private String subscriptionName;
    @XmlElement(name = "subscriptionDescription")
    private String subscriptionDescription;
    @XmlElement(name = "subscriptionProductId")
    private String subscriptionProductId;
    @XmlElement(name = "subscriptionOrderId")
    private String subscriptionOrderId;
    @XmlElement(name = "subscriptionOrderEntryId")
    private String subscriptionOrderEntryId;
    @XmlElement(name = "billingSystemId")
    private String billingSystemId;
    @XmlElement(name = "subscriptionStartDate")
    @XmlSchemaType(name = "date")
    private Date subscriptionStartDate;
    @XmlElement(name = "subscriptionEndDate")
    @XmlSchemaType(name = "date")
    private Date subscriptionEndDate;
    @XmlElement(name = "subscriptionStatus")
    private String subscriptionStatus;
    @XmlElement(name = "cancellationPossible")
    private Boolean cancellationPossible;
    @XmlElement(name = "billingFrequency")
    private String billingFrequency;
    @XmlElement(name = "contractDuration")
    private String contractDuration;
    @XmlElement(name = "orderDate")
    private Date orderDate;
    @XmlElement(name = "cancelDate")
    private Date cancelDate;
    @XmlElement(name = "comments")
    private String comments;
    @XmlElement(name = "paymentMethod")
    private CisPaymentMethod paymentMethod;
    @XmlElement(name = "autoRenewal")
    private Boolean autoRenewal;


    public String getCurrency()
    {
        return this.currency;
    }


    public void setCurrency(String currency)
    {
        this.currency = currency;
    }


    public String getComments()
    {
        return this.comments;
    }


    public void setComments(String comments)
    {
        this.comments = comments;
    }


    public String getMerchantAccountId()
    {
        return this.merchantAccountId;
    }


    public void setMerchantAccountId(String merchantAccountId)
    {
        this.merchantAccountId = merchantAccountId;
    }


    public String getSubscriptionId()
    {
        return this.subscriptionId;
    }


    public void setSubscriptionId(String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
    }


    public String getSubscriptionOrderId()
    {
        return this.subscriptionOrderId;
    }


    public void setSubscriptionOrderId(String subscriptionOrderId)
    {
        this.subscriptionOrderId = subscriptionOrderId;
    }


    public String getSubscriptionOrderEntryId()
    {
        return this.subscriptionOrderEntryId;
    }


    public void setSubscriptionOrderEntryId(String subscriptionOrderEntryId)
    {
        this.subscriptionOrderEntryId = subscriptionOrderEntryId;
    }


    public String getSubscriptionProductId()
    {
        return this.subscriptionProductId;
    }


    public void setSubscriptionProductId(String subscriptionProductId)
    {
        this.subscriptionProductId = subscriptionProductId;
    }


    public String getSubscriptionDescription()
    {
        return this.subscriptionDescription;
    }


    public void setSubscriptionDescription(String subscriptionDescription)
    {
        this.subscriptionDescription = subscriptionDescription;
    }


    public Date getSubscriptionStartDate()
    {
        return (this.subscriptionStartDate == null) ? null : new Date(this.subscriptionStartDate.getTime());
    }


    public void setSubscriptionStartDate(Date subscriptionStartDate)
    {
        this.subscriptionStartDate = subscriptionStartDate;
    }


    public Date getSubscriptionEndDate()
    {
        return (this.subscriptionEndDate == null) ? null : new Date(this.subscriptionEndDate.getTime());
    }


    public void setSubscriptionEndDate(Date subscriptionEndDate)
    {
        this.subscriptionEndDate = subscriptionEndDate;
    }


    public String getSubscriptionStatus()
    {
        return this.subscriptionStatus;
    }


    public void setSubscriptionStatus(String subscriptionStatus)
    {
        this.subscriptionStatus = subscriptionStatus;
    }


    public Boolean getCancellationPossible()
    {
        return this.cancellationPossible;
    }


    public void setCancellationPossible(Boolean cancellationPossible)
    {
        this.cancellationPossible = cancellationPossible;
    }


    public String getBillingFrequency()
    {
        return this.billingFrequency;
    }


    public void setBillingFrequency(String billingFrequency)
    {
        this.billingFrequency = billingFrequency;
    }


    public String getContractDuration()
    {
        return this.contractDuration;
    }


    public void setContractDuration(String contractDuration)
    {
        this.contractDuration = contractDuration;
    }


    public Date getOrderDate()
    {
        return (this.orderDate == null) ? null : new Date(this.orderDate.getTime());
    }


    public void setOrderDate(Date orderDate)
    {
        this.orderDate = orderDate;
    }


    public Date getCancelDate()
    {
        return (this.cancelDate == null) ? null : new Date(this.cancelDate.getTime());
    }


    public void setCancelDate(Date cancelDate)
    {
        this.cancelDate = cancelDate;
    }


    public String getSubscriptionName()
    {
        return this.subscriptionName;
    }


    public void setSubscriptionName(String subscriptionName)
    {
        this.subscriptionName = subscriptionName;
    }


    public String getBillingSystemId()
    {
        return this.billingSystemId;
    }


    public void setBillingSystemId(String billingSystemId)
    {
        this.billingSystemId = billingSystemId;
    }


    public CisPaymentMethod getPaymentMethod()
    {
        return this.paymentMethod;
    }


    public void setPaymentMethod(CisPaymentMethod paymentMethod)
    {
        this.paymentMethod = paymentMethod;
    }


    public Boolean getAutoRenewal()
    {
        return this.autoRenewal;
    }


    public void setAutoRenewal(Boolean autoRenewal)
    {
        this.autoRenewal = autoRenewal;
    }


    public String getSubscriptionStartDateInReadableFormat()
    {
        return formatDateToString(this.subscriptionStartDate);
    }


    public String getSubscriptionEndDateInReadableFormat()
    {
        return formatDateToString(this.subscriptionEndDate);
    }


    private String formatDateToString(Date rawDate)
    {
        if(rawDate == null)
        {
            return null;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(rawDate.getTime());
        return df.format(date);
    }
}
