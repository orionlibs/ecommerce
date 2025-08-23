package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.commercefacades.user.data.AddressData;
import java.io.Serializable;

public class CCPaymentInfoData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String accountHolderName;
    private String cardType;
    private CardTypeData cardTypeData;
    private String cardNumber;
    private String startMonth;
    private String startYear;
    private String expiryMonth;
    private String expiryYear;
    private String issueNumber;
    private String subscriptionId;
    private boolean saved;
    private boolean defaultPaymentInfo;
    private AddressData billingAddress;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setAccountHolderName(String accountHolderName)
    {
        this.accountHolderName = accountHolderName;
    }


    public String getAccountHolderName()
    {
        return this.accountHolderName;
    }


    public void setCardType(String cardType)
    {
        this.cardType = cardType;
    }


    public String getCardType()
    {
        return this.cardType;
    }


    public void setCardTypeData(CardTypeData cardTypeData)
    {
        this.cardTypeData = cardTypeData;
    }


    public CardTypeData getCardTypeData()
    {
        return this.cardTypeData;
    }


    public void setCardNumber(String cardNumber)
    {
        this.cardNumber = cardNumber;
    }


    public String getCardNumber()
    {
        return this.cardNumber;
    }


    public void setStartMonth(String startMonth)
    {
        this.startMonth = startMonth;
    }


    public String getStartMonth()
    {
        return this.startMonth;
    }


    public void setStartYear(String startYear)
    {
        this.startYear = startYear;
    }


    public String getStartYear()
    {
        return this.startYear;
    }


    public void setExpiryMonth(String expiryMonth)
    {
        this.expiryMonth = expiryMonth;
    }


    public String getExpiryMonth()
    {
        return this.expiryMonth;
    }


    public void setExpiryYear(String expiryYear)
    {
        this.expiryYear = expiryYear;
    }


    public String getExpiryYear()
    {
        return this.expiryYear;
    }


    public void setIssueNumber(String issueNumber)
    {
        this.issueNumber = issueNumber;
    }


    public String getIssueNumber()
    {
        return this.issueNumber;
    }


    public void setSubscriptionId(String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
    }


    public String getSubscriptionId()
    {
        return this.subscriptionId;
    }


    public void setSaved(boolean saved)
    {
        this.saved = saved;
    }


    public boolean isSaved()
    {
        return this.saved;
    }


    public void setDefaultPaymentInfo(boolean defaultPaymentInfo)
    {
        this.defaultPaymentInfo = defaultPaymentInfo;
    }


    public boolean isDefaultPaymentInfo()
    {
        return this.defaultPaymentInfo;
    }


    public void setBillingAddress(AddressData billingAddress)
    {
        this.billingAddress = billingAddress;
    }


    public AddressData getBillingAddress()
    {
        return this.billingAddress;
    }
}
