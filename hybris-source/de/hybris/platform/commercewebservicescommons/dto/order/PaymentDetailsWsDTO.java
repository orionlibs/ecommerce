package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "PaymentDetails", description = "Representation of a Payment Details")
public class PaymentDetailsWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "id", value = "Unique identifier of payment detail")
    private String id;
    @ApiModelProperty(name = "accountHolderName", value = "Name of account holder")
    private String accountHolderName;
    @ApiModelProperty(name = "cardType", value = "Type of payment card")
    private CardTypeWsDTO cardType;
    @ApiModelProperty(name = "cardNumber", value = "Payment card number")
    private String cardNumber;
    @ApiModelProperty(name = "startMonth", value = "Start month from which payment is valid")
    private String startMonth;
    @ApiModelProperty(name = "startYear", value = "Start year from which payment is valid")
    private String startYear;
    @ApiModelProperty(name = "expiryMonth", value = "Month of expiration of payment")
    private String expiryMonth;
    @ApiModelProperty(name = "expiryYear", value = "Year of expiration of payment")
    private String expiryYear;
    @ApiModelProperty(name = "issueNumber", value = "Issue number")
    private String issueNumber;
    @ApiModelProperty(name = "subscriptionId", value = "Identifier of subscription")
    private String subscriptionId;
    @ApiModelProperty(name = "saved", value = "Flag to mark if payment is saved one")
    private Boolean saved;
    @ApiModelProperty(name = "defaultPayment", value = "Flag to mark if payment the default one")
    private Boolean defaultPayment;
    @ApiModelProperty(name = "billingAddress", value = "Address details to be considered as billing address")
    private AddressWsDTO billingAddress;


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


    public void setCardType(CardTypeWsDTO cardType)
    {
        this.cardType = cardType;
    }


    public CardTypeWsDTO getCardType()
    {
        return this.cardType;
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


    public void setSaved(Boolean saved)
    {
        this.saved = saved;
    }


    public Boolean getSaved()
    {
        return this.saved;
    }


    public void setDefaultPayment(Boolean defaultPayment)
    {
        this.defaultPayment = defaultPayment;
    }


    public Boolean getDefaultPayment()
    {
        return this.defaultPayment;
    }


    public void setBillingAddress(AddressWsDTO billingAddress)
    {
        this.billingAddress = billingAddress;
    }


    public AddressWsDTO getBillingAddress()
    {
        return this.billingAddress;
    }
}
