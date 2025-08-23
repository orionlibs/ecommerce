package de.hybris.platform.acceleratorservices.payment.data;

import java.io.Serializable;

public class CustomerShipToData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String shipToCity;
    private String shipToCompany;
    private String shipToCountry;
    private String shipToFirstName;
    private String shipToLastName;
    private String shipToPhoneNumber;
    private String shipToPostalCode;
    private String shipToShippingMethod;
    private String shipToState;
    private String shipToStreet1;
    private String shipToStreet2;


    public void setShipToCity(String shipToCity)
    {
        this.shipToCity = shipToCity;
    }


    public String getShipToCity()
    {
        return this.shipToCity;
    }


    public void setShipToCompany(String shipToCompany)
    {
        this.shipToCompany = shipToCompany;
    }


    public String getShipToCompany()
    {
        return this.shipToCompany;
    }


    public void setShipToCountry(String shipToCountry)
    {
        this.shipToCountry = shipToCountry;
    }


    public String getShipToCountry()
    {
        return this.shipToCountry;
    }


    public void setShipToFirstName(String shipToFirstName)
    {
        this.shipToFirstName = shipToFirstName;
    }


    public String getShipToFirstName()
    {
        return this.shipToFirstName;
    }


    public void setShipToLastName(String shipToLastName)
    {
        this.shipToLastName = shipToLastName;
    }


    public String getShipToLastName()
    {
        return this.shipToLastName;
    }


    public void setShipToPhoneNumber(String shipToPhoneNumber)
    {
        this.shipToPhoneNumber = shipToPhoneNumber;
    }


    public String getShipToPhoneNumber()
    {
        return this.shipToPhoneNumber;
    }


    public void setShipToPostalCode(String shipToPostalCode)
    {
        this.shipToPostalCode = shipToPostalCode;
    }


    public String getShipToPostalCode()
    {
        return this.shipToPostalCode;
    }


    public void setShipToShippingMethod(String shipToShippingMethod)
    {
        this.shipToShippingMethod = shipToShippingMethod;
    }


    public String getShipToShippingMethod()
    {
        return this.shipToShippingMethod;
    }


    public void setShipToState(String shipToState)
    {
        this.shipToState = shipToState;
    }


    public String getShipToState()
    {
        return this.shipToState;
    }


    public void setShipToStreet1(String shipToStreet1)
    {
        this.shipToStreet1 = shipToStreet1;
    }


    public String getShipToStreet1()
    {
        return this.shipToStreet1;
    }


    public void setShipToStreet2(String shipToStreet2)
    {
        this.shipToStreet2 = shipToStreet2;
    }


    public String getShipToStreet2()
    {
        return this.shipToStreet2;
    }
}
