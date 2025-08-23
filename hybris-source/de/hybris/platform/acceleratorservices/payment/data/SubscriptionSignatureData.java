package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class SubscriptionSignatureData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private BigDecimal recurringSubscriptionInfoAmount;
    private String recurringSubscriptionInfoStartDate;
    private String recurringSubscriptionInfoFrequency;
    private Integer recurringSubscriptionInfoNumberOfPayments;
    private Boolean recurringSubscriptionInfoAutomaticRenew;
    private String sharedSecret;
    private String recurringSubscriptionInfoNumberOfPaymentsPublicSignature;
    private String recurringSubscriptionInfoAmountPublicSignature;
    private String recurringSubscriptionInfoStartDatePublicSignature;
    private String recurringSubscriptionInfoAutomaticRenewPublicSignature;
    private String recurringSubscriptionInfoFrequencyPublicSignature;


    public void setRecurringSubscriptionInfoAmount(BigDecimal recurringSubscriptionInfoAmount)
    {
        this.recurringSubscriptionInfoAmount = recurringSubscriptionInfoAmount;
    }


    public BigDecimal getRecurringSubscriptionInfoAmount()
    {
        return this.recurringSubscriptionInfoAmount;
    }


    public void setRecurringSubscriptionInfoStartDate(String recurringSubscriptionInfoStartDate)
    {
        this.recurringSubscriptionInfoStartDate = recurringSubscriptionInfoStartDate;
    }


    public String getRecurringSubscriptionInfoStartDate()
    {
        return this.recurringSubscriptionInfoStartDate;
    }


    public void setRecurringSubscriptionInfoFrequency(String recurringSubscriptionInfoFrequency)
    {
        this.recurringSubscriptionInfoFrequency = recurringSubscriptionInfoFrequency;
    }


    public String getRecurringSubscriptionInfoFrequency()
    {
        return this.recurringSubscriptionInfoFrequency;
    }


    public void setRecurringSubscriptionInfoNumberOfPayments(Integer recurringSubscriptionInfoNumberOfPayments)
    {
        this.recurringSubscriptionInfoNumberOfPayments = recurringSubscriptionInfoNumberOfPayments;
    }


    public Integer getRecurringSubscriptionInfoNumberOfPayments()
    {
        return this.recurringSubscriptionInfoNumberOfPayments;
    }


    public void setRecurringSubscriptionInfoAutomaticRenew(Boolean recurringSubscriptionInfoAutomaticRenew)
    {
        this.recurringSubscriptionInfoAutomaticRenew = recurringSubscriptionInfoAutomaticRenew;
    }


    public Boolean getRecurringSubscriptionInfoAutomaticRenew()
    {
        return this.recurringSubscriptionInfoAutomaticRenew;
    }


    public void setSharedSecret(String sharedSecret)
    {
        this.sharedSecret = sharedSecret;
    }


    public String getSharedSecret()
    {
        return this.sharedSecret;
    }


    public void setRecurringSubscriptionInfoNumberOfPaymentsPublicSignature(String recurringSubscriptionInfoNumberOfPaymentsPublicSignature)
    {
        this.recurringSubscriptionInfoNumberOfPaymentsPublicSignature = recurringSubscriptionInfoNumberOfPaymentsPublicSignature;
    }


    public String getRecurringSubscriptionInfoNumberOfPaymentsPublicSignature()
    {
        return this.recurringSubscriptionInfoNumberOfPaymentsPublicSignature;
    }


    public void setRecurringSubscriptionInfoAmountPublicSignature(String recurringSubscriptionInfoAmountPublicSignature)
    {
        this.recurringSubscriptionInfoAmountPublicSignature = recurringSubscriptionInfoAmountPublicSignature;
    }


    public String getRecurringSubscriptionInfoAmountPublicSignature()
    {
        return this.recurringSubscriptionInfoAmountPublicSignature;
    }


    public void setRecurringSubscriptionInfoStartDatePublicSignature(String recurringSubscriptionInfoStartDatePublicSignature)
    {
        this.recurringSubscriptionInfoStartDatePublicSignature = recurringSubscriptionInfoStartDatePublicSignature;
    }


    public String getRecurringSubscriptionInfoStartDatePublicSignature()
    {
        return this.recurringSubscriptionInfoStartDatePublicSignature;
    }


    public void setRecurringSubscriptionInfoAutomaticRenewPublicSignature(String recurringSubscriptionInfoAutomaticRenewPublicSignature)
    {
        this.recurringSubscriptionInfoAutomaticRenewPublicSignature = recurringSubscriptionInfoAutomaticRenewPublicSignature;
    }


    public String getRecurringSubscriptionInfoAutomaticRenewPublicSignature()
    {
        return this.recurringSubscriptionInfoAutomaticRenewPublicSignature;
    }


    public void setRecurringSubscriptionInfoFrequencyPublicSignature(String recurringSubscriptionInfoFrequencyPublicSignature)
    {
        this.recurringSubscriptionInfoFrequencyPublicSignature = recurringSubscriptionInfoFrequencyPublicSignature;
    }


    public String getRecurringSubscriptionInfoFrequencyPublicSignature()
    {
        return this.recurringSubscriptionInfoFrequencyPublicSignature;
    }
}
