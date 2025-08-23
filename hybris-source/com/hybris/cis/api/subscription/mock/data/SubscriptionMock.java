package com.hybris.cis.api.subscription.mock.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SubscriptionMock implements Serializable
{
    private static final long serialVersionUID = 6547406724518966871L;
    private String merchantAccountId;
    private String currency;
    private String subscriptionId;
    private String subscriptionName;
    private String subscriptionDescription;
    private String subscriptionProductId;
    private String subscriptionOrderId;
    private String subscriptionOrderEntryId;
    private String billingSystemId;
    private Date subscriptionStartDate;
    private Date subscriptionEndDate;
    private String subscriptionStatus;
    private Boolean cancellationPossible;
    private Boolean subscriptionAutoRenewal;
    private String billingFrequency;
    private String contractDuration;
    private Date orderDate;
    private Date cancelDate;
    private String comments;
    private PaymentMethodMock paymentMethod;
    private Map<String, String> vendorParameters;
    private String subscriptionPlanName;
    private String subscriptionPlanId;
    private List<ChargeMock> subscriptionPlanCharges;
    private List<UsageChargeMock> subscriptionPlanUsageCharges;


    public static long getSerialVersionUID()
    {
        return 6547406724518966871L;
    }


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
        this.subscriptionStartDate = (subscriptionStartDate == null) ? null : new Date(subscriptionStartDate.getTime());
    }


    public Date getSubscriptionEndDate()
    {
        return (this.subscriptionEndDate == null) ? null : new Date(this.subscriptionEndDate.getTime());
    }


    public void setSubscriptionEndDate(Date subscriptionEndDate)
    {
        this.subscriptionEndDate = (subscriptionEndDate == null) ? null : new Date(subscriptionEndDate.getTime());
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
        this.orderDate = (orderDate == null) ? null : new Date(orderDate.getTime());
    }


    public Date getCancelDate()
    {
        return (this.cancelDate == null) ? null : new Date(this.cancelDate.getTime());
    }


    public void setCancelDate(Date cancelDate)
    {
        this.cancelDate = (cancelDate == null) ? null : new Date(cancelDate.getTime());
    }


    public Map<String, String> getVendorParameters()
    {
        return this.vendorParameters;
    }


    public void setVendorParameters(Map<String, String> vendorParameters)
    {
        this.vendorParameters = vendorParameters;
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


    public PaymentMethodMock getPaymentMethod()
    {
        return this.paymentMethod;
    }


    public void setPaymentMethod(PaymentMethodMock paymentMethod)
    {
        this.paymentMethod = paymentMethod;
    }


    public Boolean getSubscriptionAutoRenewal()
    {
        return this.subscriptionAutoRenewal;
    }


    public void setSubscriptionAutoRenewal(Boolean subscriptionAutoRenewal)
    {
        this.subscriptionAutoRenewal = subscriptionAutoRenewal;
    }


    public String getSubscriptionPlanName()
    {
        return this.subscriptionPlanName;
    }


    public void setSubscriptionPlanName(String subscriptionPlanName)
    {
        this.subscriptionPlanName = subscriptionPlanName;
    }


    public String getSubscriptionPlanId()
    {
        return this.subscriptionPlanId;
    }


    public void setSubscriptionPlanId(String subscriptionPlanId)
    {
        this.subscriptionPlanId = subscriptionPlanId;
    }


    public List<ChargeMock> getSubscriptionPlanCharges()
    {
        return this.subscriptionPlanCharges;
    }


    public void setSubscriptionPlanCharges(List<ChargeMock> subscriptionPlanCharges)
    {
        this.subscriptionPlanCharges = subscriptionPlanCharges;
    }


    public List<UsageChargeMock> getSubscriptionPlanUsageCharges()
    {
        return this.subscriptionPlanUsageCharges;
    }


    public void setSubscriptionPlanUsageCharges(List<UsageChargeMock> subscriptionPlanUsageCharges)
    {
        this.subscriptionPlanUsageCharges = subscriptionPlanUsageCharges;
    }
}
