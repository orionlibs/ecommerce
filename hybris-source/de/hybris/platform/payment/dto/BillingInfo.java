package de.hybris.platform.payment.dto;

import java.io.Serializable;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.SerializationUtils;

public class BillingInfo implements Serializable
{
    private String city;
    private String country;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String postalCode;
    private String state;
    private String street1;
    private String street2;
    private String ipAddress;
    private String region;


    public void setCity(String city)
    {
        this.city = city;
    }


    public String getCity()
    {
        return this.city;
    }


    public void setCountry(String country)
    {
        this.country = country;
    }


    public String getCountry()
    {
        return this.country;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getEmail()
    {
        return this.email;
    }


    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }


    public String getFirstName()
    {
        return this.firstName;
    }


    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }


    public String getLastName()
    {
        return this.lastName;
    }


    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }


    public String getPhoneNumber()
    {
        return this.phoneNumber;
    }


    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }


    public String getPostalCode()
    {
        return this.postalCode;
    }


    public void setState(String state)
    {
        this.state = state;
    }


    public String getState()
    {
        return this.state;
    }


    public void setStreet1(String street1)
    {
        this.street1 = street1;
    }


    public String getStreet1()
    {
        return this.street1;
    }


    public void setStreet2(String street2)
    {
        this.street2 = street2;
    }


    public String getStreet2()
    {
        return this.street2;
    }


    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }


    public String getIpAddress()
    {
        return this.ipAddress;
    }


    public void setRegion(String region)
    {
        this.region = region;
    }


    public String getRegion()
    {
        return this.region;
    }


    public void copy(BillingInfo orig)
    {
        try
        {
            BillingInfo deepCopy = (BillingInfo)SerializationUtils.clone(orig);
            BeanUtils.copyProperties(this, deepCopy);
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Failed to copy BillingInfo", ex);
        }
    }
}
