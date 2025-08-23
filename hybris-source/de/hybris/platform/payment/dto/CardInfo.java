package de.hybris.platform.payment.dto;

import de.hybris.platform.core.enums.CreditCardType;
import java.io.Serializable;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.SerializationUtils;

public class CardInfo extends BasicCardInfo
{
    private String cardHolderFullName;
    private String issueNumber;
    private Integer issueMonth;
    private Integer issueYear;
    private CreditCardType cardType;
    private BillingInfo billingInfo;
    private String cv2Number;


    public void setCardHolderFullName(String cardHolderFullName)
    {
        this.cardHolderFullName = cardHolderFullName;
    }


    public String getCardHolderFullName()
    {
        return this.cardHolderFullName;
    }


    public void setIssueNumber(String issueNumber)
    {
        this.issueNumber = issueNumber;
    }


    public String getIssueNumber()
    {
        return this.issueNumber;
    }


    public void setIssueMonth(Integer issueMonth)
    {
        this.issueMonth = issueMonth;
    }


    public Integer getIssueMonth()
    {
        return this.issueMonth;
    }


    public void setIssueYear(Integer issueYear)
    {
        this.issueYear = issueYear;
    }


    public Integer getIssueYear()
    {
        return this.issueYear;
    }


    public void setCardType(CreditCardType cardType)
    {
        this.cardType = cardType;
    }


    public CreditCardType getCardType()
    {
        return this.cardType;
    }


    public void setBillingInfo(BillingInfo billingInfo)
    {
        this.billingInfo = billingInfo;
    }


    public BillingInfo getBillingInfo()
    {
        return this.billingInfo;
    }


    public void setCv2Number(String cv2Number)
    {
        this.cv2Number = cv2Number;
    }


    public String getCv2Number()
    {
        return this.cv2Number;
    }


    public void copy(CardInfo orig)
    {
        try
        {
            CardInfo deepCopy = (CardInfo)SerializationUtils.clone((Serializable)orig);
            BeanUtils.copyProperties(this, deepCopy);
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Failed to copy CardInfo", ex);
        }
    }
}
