package de.hybris.platform.commercewebservicescommons.dto.store;

import java.io.Serializable;

public class ShippingCarrierWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String accountReferenceNumber;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setAccountReferenceNumber(String accountReferenceNumber)
    {
        this.accountReferenceNumber = accountReferenceNumber;
    }


    public String getAccountReferenceNumber()
    {
        return this.accountReferenceNumber;
    }
}
