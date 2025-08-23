package de.hybris.platform.sap.sapcpiadapter.data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sapCpiOrderAddress")
public class SapCpiOrderAddress implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String documentAddressId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String middleName2;
    private String street;
    private String city;
    private String district;
    private String building;
    private String apartment;
    private String pobox;
    private String faxNumber;
    private String titleCode;
    private String telNumber;
    private String houseNumber;
    private String postalCode;
    private String regionIsoCode;
    private String countryIsoCode;
    private String email;
    private String languageIsoCode;


    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }


    public String getOrderId()
    {
        return this.orderId;
    }


    public void setDocumentAddressId(String documentAddressId)
    {
        this.documentAddressId = documentAddressId;
    }


    public String getDocumentAddressId()
    {
        return this.documentAddressId;
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


    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }


    public String getMiddleName()
    {
        return this.middleName;
    }


    public void setMiddleName2(String middleName2)
    {
        this.middleName2 = middleName2;
    }


    public String getMiddleName2()
    {
        return this.middleName2;
    }


    public void setStreet(String street)
    {
        this.street = street;
    }


    public String getStreet()
    {
        return this.street;
    }


    public void setCity(String city)
    {
        this.city = city;
    }


    public String getCity()
    {
        return this.city;
    }


    public void setDistrict(String district)
    {
        this.district = district;
    }


    public String getDistrict()
    {
        return this.district;
    }


    public void setBuilding(String building)
    {
        this.building = building;
    }


    public String getBuilding()
    {
        return this.building;
    }


    public void setApartment(String apartment)
    {
        this.apartment = apartment;
    }


    public String getApartment()
    {
        return this.apartment;
    }


    public void setPobox(String pobox)
    {
        this.pobox = pobox;
    }


    public String getPobox()
    {
        return this.pobox;
    }


    public void setFaxNumber(String faxNumber)
    {
        this.faxNumber = faxNumber;
    }


    public String getFaxNumber()
    {
        return this.faxNumber;
    }


    public void setTitleCode(String titleCode)
    {
        this.titleCode = titleCode;
    }


    public String getTitleCode()
    {
        return this.titleCode;
    }


    public void setTelNumber(String telNumber)
    {
        this.telNumber = telNumber;
    }


    public String getTelNumber()
    {
        return this.telNumber;
    }


    public void setHouseNumber(String houseNumber)
    {
        this.houseNumber = houseNumber;
    }


    public String getHouseNumber()
    {
        return this.houseNumber;
    }


    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }


    public String getPostalCode()
    {
        return this.postalCode;
    }


    public void setRegionIsoCode(String regionIsoCode)
    {
        this.regionIsoCode = regionIsoCode;
    }


    public String getRegionIsoCode()
    {
        return this.regionIsoCode;
    }


    public void setCountryIsoCode(String countryIsoCode)
    {
        this.countryIsoCode = countryIsoCode;
    }


    public String getCountryIsoCode()
    {
        return this.countryIsoCode;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getEmail()
    {
        return this.email;
    }


    public void setLanguageIsoCode(String languageIsoCode)
    {
        this.languageIsoCode = languageIsoCode;
    }


    public String getLanguageIsoCode()
    {
        return this.languageIsoCode;
    }
}
