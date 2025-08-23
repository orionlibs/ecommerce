package de.hybris.platform.payment.dto;

import java.io.Serializable;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.SerializationUtils;

public class BasicCardInfo implements Serializable
{
    private String cardNumber;
    private Integer expirationMonth;
    private Integer expirationYear;


    public void setCardNumber(String cardNumber)
    {
        this.cardNumber = cardNumber;
    }


    public String getCardNumber()
    {
        return this.cardNumber;
    }


    public void setExpirationMonth(Integer expirationMonth)
    {
        this.expirationMonth = expirationMonth;
    }


    public Integer getExpirationMonth()
    {
        return this.expirationMonth;
    }


    public void setExpirationYear(Integer expirationYear)
    {
        this.expirationYear = expirationYear;
    }


    public Integer getExpirationYear()
    {
        return this.expirationYear;
    }


    public void copy(BasicCardInfo orig)
    {
        try
        {
            BasicCardInfo deepCopy = (BasicCardInfo)SerializationUtils.clone(orig);
            BeanUtils.copyProperties(this, deepCopy);
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Failed to copy BasicCardInfo", ex);
        }
    }
}
