package de.hybris.platform.b2bcommercefacades.data;

import java.io.Serializable;

public class B2BRegistrationData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String titleCode;
    private String name;
    private String email;
    private String position;
    private String telephone;
    private String telephoneExtension;
    private String companyName;
    private String companyAddressStreet;
    private String companyAddressStreetLine2;
    private String companyAddressCity;
    private String companyAddressPostalCode;
    private String companyAddressRegion;
    private String companyAddressCountryIso;
    private String message;


    public void setTitleCode(String titleCode)
    {
        this.titleCode = titleCode;
    }


    public String getTitleCode()
    {
        return this.titleCode;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getEmail()
    {
        return this.email;
    }


    public void setPosition(String position)
    {
        this.position = position;
    }


    public String getPosition()
    {
        return this.position;
    }


    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }


    public String getTelephone()
    {
        return this.telephone;
    }


    public void setTelephoneExtension(String telephoneExtension)
    {
        this.telephoneExtension = telephoneExtension;
    }


    public String getTelephoneExtension()
    {
        return this.telephoneExtension;
    }


    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }


    public String getCompanyName()
    {
        return this.companyName;
    }


    public void setCompanyAddressStreet(String companyAddressStreet)
    {
        this.companyAddressStreet = companyAddressStreet;
    }


    public String getCompanyAddressStreet()
    {
        return this.companyAddressStreet;
    }


    public void setCompanyAddressStreetLine2(String companyAddressStreetLine2)
    {
        this.companyAddressStreetLine2 = companyAddressStreetLine2;
    }


    public String getCompanyAddressStreetLine2()
    {
        return this.companyAddressStreetLine2;
    }


    public void setCompanyAddressCity(String companyAddressCity)
    {
        this.companyAddressCity = companyAddressCity;
    }


    public String getCompanyAddressCity()
    {
        return this.companyAddressCity;
    }


    public void setCompanyAddressPostalCode(String companyAddressPostalCode)
    {
        this.companyAddressPostalCode = companyAddressPostalCode;
    }


    public String getCompanyAddressPostalCode()
    {
        return this.companyAddressPostalCode;
    }


    public void setCompanyAddressRegion(String companyAddressRegion)
    {
        this.companyAddressRegion = companyAddressRegion;
    }


    public String getCompanyAddressRegion()
    {
        return this.companyAddressRegion;
    }


    public void setCompanyAddressCountryIso(String companyAddressCountryIso)
    {
        this.companyAddressCountryIso = companyAddressCountryIso;
    }


    public String getCompanyAddressCountryIso()
    {
        return this.companyAddressCountryIso;
    }


    public void setMessage(String message)
    {
        this.message = message;
    }


    public String getMessage()
    {
        return this.message;
    }
}
