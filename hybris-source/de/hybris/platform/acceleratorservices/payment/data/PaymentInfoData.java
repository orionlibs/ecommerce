package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;

public class PaymentInfoData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String cardAccountNumber;
    private String cardCardType;
    private String cardCvNumber;
    private Integer cardExpirationMonth;
    private Integer cardExpirationYear;
    private String cardIssueNumber;
    private String cardStartMonth;
    private String cardStartYear;
    private String paymentOption;
    private String cardAccountHolderName;


    public void setCardAccountNumber(String cardAccountNumber)
    {
        this.cardAccountNumber = cardAccountNumber;
    }


    public String getCardAccountNumber()
    {
        return this.cardAccountNumber;
    }


    public void setCardCardType(String cardCardType)
    {
        this.cardCardType = cardCardType;
    }


    public String getCardCardType()
    {
        return this.cardCardType;
    }


    public void setCardCvNumber(String cardCvNumber)
    {
        this.cardCvNumber = cardCvNumber;
    }


    public String getCardCvNumber()
    {
        return this.cardCvNumber;
    }


    public void setCardExpirationMonth(Integer cardExpirationMonth)
    {
        this.cardExpirationMonth = cardExpirationMonth;
    }


    public Integer getCardExpirationMonth()
    {
        return this.cardExpirationMonth;
    }


    public void setCardExpirationYear(Integer cardExpirationYear)
    {
        this.cardExpirationYear = cardExpirationYear;
    }


    public Integer getCardExpirationYear()
    {
        return this.cardExpirationYear;
    }


    public void setCardIssueNumber(String cardIssueNumber)
    {
        this.cardIssueNumber = cardIssueNumber;
    }


    public String getCardIssueNumber()
    {
        return this.cardIssueNumber;
    }


    public void setCardStartMonth(String cardStartMonth)
    {
        this.cardStartMonth = cardStartMonth;
    }


    public String getCardStartMonth()
    {
        return this.cardStartMonth;
    }


    public void setCardStartYear(String cardStartYear)
    {
        this.cardStartYear = cardStartYear;
    }


    public String getCardStartYear()
    {
        return this.cardStartYear;
    }


    public void setPaymentOption(String paymentOption)
    {
        this.paymentOption = paymentOption;
    }


    public String getPaymentOption()
    {
        return this.paymentOption;
    }


    public void setCardAccountHolderName(String cardAccountHolderName)
    {
        this.cardAccountHolderName = cardAccountHolderName;
    }


    public String getCardAccountHolderName()
    {
        return this.cardAccountHolderName;
    }
}
