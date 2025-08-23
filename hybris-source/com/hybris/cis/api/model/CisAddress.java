package com.hybris.cis.api.model;

import com.hybris.cis.api.validation.XSSSafe;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "address")
@XmlAccessorType(XmlAccessType.FIELD)
public class CisAddress
{
    public static final int STATE_LETTER_FORMAT = 2;
    @XmlElement(name = "type")
    private CisAddressType type;
    @XmlElement(name = "title")
    @XSSSafe
    private String title;
    @XmlElement(name = "firstName")
    @XSSSafe
    private String firstName;
    @XmlElement(name = "lastName")
    @XSSSafe
    private String lastName;
    @XmlElement(name = "email")
    @XSSSafe
    private String email;
    @XmlElement(name = "addressLine1")
    @XSSSafe
    private String addressLine1;
    @XmlElement(name = "addressLine2")
    @XSSSafe
    private String addressLine2;
    @XmlElement(name = "addressLine3")
    @XSSSafe
    private String addressLine3;
    @XmlElement(name = "addressLine4")
    @XSSSafe
    private String addressLine4;
    @XmlElement(name = "zipCode")
    @XSSSafe
    private String zipCode;
    @XmlElement(name = "city")
    @XSSSafe
    private String city;
    @XmlElement(name = "state")
    @XSSSafe
    private String state;
    @XmlElement(name = "country")
    @XSSSafe
    private String country;
    @XmlElement(name = "phone")
    @XSSSafe
    private String phone;
    @XmlElement(name = "company")
    @XSSSafe
    private String company;
    @XmlElement(name = "longitude")
    @XSSSafe
    private String longitude;
    @XmlElement(name = "latitude")
    @XSSSafe
    private String latitude;
    @XmlElement(name = "facilityType")
    @XSSSafe
    private String facilityType;
    @XmlElement(name = "facilityName")
    @XSSSafe
    private String facilityName;
    @XmlElement(name = "faxNumber")
    @XSSSafe
    private String faxNumber;
    @XmlElement(name = "vendorParameters")
    @Valid
    private AnnotationHashMap vendorParameters;


    public CisAddress()
    {
    }


    public CisAddress(String addressLine1, String zipCode, String city, String state, String country)
    {
        this.addressLine1 = addressLine1;
        this.zipCode = zipCode;
        this.city = city;
        this.state = state;
        this.country = country;
    }


    public String getAddressLine1()
    {
        return this.addressLine1;
    }


    public void setAddressLine1(String addressLine1)
    {
        this.addressLine1 = addressLine1;
    }


    public String getAddressLine2()
    {
        return this.addressLine2;
    }


    public void setAddressLine2(String addressLine2)
    {
        this.addressLine2 = addressLine2;
    }


    public String getAddressLine3()
    {
        return this.addressLine3;
    }


    public void setAddressLine3(String addressLine3)
    {
        this.addressLine3 = addressLine3;
    }


    public String getAddressLine4()
    {
        return this.addressLine4;
    }


    public void setAddressLine4(String addressLine4)
    {
        this.addressLine4 = addressLine4;
    }


    public String getLongitude()
    {
        return this.longitude;
    }


    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }


    public String getLatitude()
    {
        return this.latitude;
    }


    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }


    public String getZipCode()
    {
        return this.zipCode;
    }


    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }


    public String getCity()
    {
        return this.city;
    }


    public void setCity(String city)
    {
        this.city = city;
    }


    public String getStateAsTwoLetter()
    {
        if(this.state != null && this.state.length() > 2 && this.state.contains("-"))
        {
            return this.state.substring(3);
        }
        return this.state;
    }


    public String getState()
    {
        return this.state;
    }


    public void setState(String state)
    {
        this.state = state;
    }


    public String getCountry()
    {
        return this.country;
    }


    public void setCountry(String country)
    {
        this.country = country;
    }


    public CisAddressType getType()
    {
        return this.type;
    }


    public void setType(CisAddressType type)
    {
        this.type = type;
    }


    public String getFirstName()
    {
        return this.firstName;
    }


    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }


    public String getLastName()
    {
        return this.lastName;
    }


    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }


    public String getEmail()
    {
        return this.email;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getPhone()
    {
        return this.phone;
    }


    public void setPhone(String phone)
    {
        this.phone = phone;
    }


    public String getCompany()
    {
        return this.company;
    }


    public void setCompany(String company)
    {
        this.company = company;
    }


    public String getFacilityType()
    {
        return this.facilityType;
    }


    public void setFacilityType(String facilityType)
    {
        this.facilityType = facilityType;
    }


    public String getFacilityName()
    {
        return this.facilityName;
    }


    public void setFacilityName(String facilityName)
    {
        this.facilityName = facilityName;
    }


    public String getFaxNumber()
    {
        return this.faxNumber;
    }


    public void setFaxNumber(String faxNumber)
    {
        this.faxNumber = faxNumber;
    }


    public String getTitle()
    {
        return this.title;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String toString()
    {
        return "CisAddress [type=" + this.type + ", title=" + this.title + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", email=" + this.email + ", addressLine1=" + this.addressLine1 + ", addressLine2=" + this.addressLine2 + ", addressLine3=" + this.addressLine3
                        + ", addressLine4=" + this.addressLine4 + ", zipCode=" + this.zipCode + ", city=" + this.city + ", state=" + this.state + ", country=" + this.country + ", company=" + this.company + ", facilityType=" + this.facilityType + ", facilityName=" + this.facilityName + ", faxNumber="
                        + this.faxNumber + ", phone=" + this.phone + ", longitude=" + this.longitude + ", latitude=" + this.latitude + "]";
    }


    public AnnotationHashMap getVendorParameters()
    {
        return this.vendorParameters;
    }


    public void setVendorParameters(AnnotationHashMap vendorParameters)
    {
        this.vendorParameters = vendorParameters;
    }
}
