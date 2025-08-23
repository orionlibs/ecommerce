package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;

public class OrderHistoryFiltersData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String status;
    private String accountNumber;
    private String addressId;


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }


    public String getAccountNumber()
    {
        return this.accountNumber;
    }


    public void setAddressId(String addressId)
    {
        this.addressId = addressId;
    }


    public String getAddressId()
    {
        return this.addressId;
    }
}
