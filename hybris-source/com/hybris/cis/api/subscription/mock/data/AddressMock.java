package com.hybris.cis.api.subscription.mock.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.util.Assert;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "addressMock")
public class AddressMock implements Serializable
{
    private static final long serialVersionUID = -8477757149936119290L;
    @XmlElement(name = "title")
    private String title;
    @XmlElement(name = "firstName")
    private String firstName;
    @XmlElement(name = "lastName")
    private String lastName;
    @XmlElement(name = "addr1")
    private String addr1;
    @XmlElement(name = "addr2")
    private String addr2;
    @XmlElement(name = "city")
    private String city;
    @XmlElement(name = "postalCode")
    private String postalCode;
    @XmlElement(name = "country")
    private String country;
    @XmlElement(name = "emailAddress")
    private String emailAddress;


    public String getTitle()
    {
        return this.title;
    }


    public void setTitle(String title)
    {
        this.title = title;
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


    public String getAddr1()
    {
        return this.addr1;
    }


    public void setAddr1(String addr1)
    {
        this.addr1 = addr1;
    }


    public String getAddr2()
    {
        return this.addr2;
    }


    public void setAddr2(String addr2)
    {
        this.addr2 = addr2;
    }


    public String getCity()
    {
        return this.city;
    }


    public void setCity(String city)
    {
        this.city = city;
    }


    public String getPostalCode()
    {
        return this.postalCode;
    }


    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }


    public String getCountry()
    {
        return this.country;
    }


    public void setCountry(String country)
    {
        this.country = country;
    }


    public String getEmailAddress()
    {
        return this.emailAddress;
    }


    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }


    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("Firstname: ").append(this.firstName).append(System.lineSeparator());
        buf.append("Lastname:  ").append(this.lastName).append(System.lineSeparator());
        buf.append("Addr1:     ").append(this.addr1).append(System.lineSeparator());
        buf.append("Addr2:     ").append(this.addr2).append(System.lineSeparator());
        buf.append("City:      ").append(this.city).append(System.lineSeparator());
        buf.append("PostalCode:").append(this.postalCode).append(System.lineSeparator());
        buf.append("Country:   ").append(this.country).append(System.lineSeparator());
        buf.append("Email:     ").append(this.emailAddress).append(System.lineSeparator());
        return buf.toString();
    }


    public Map<String, String> getMap()
    {
        Map<String, String> map = new HashMap<>();
        map.put("titleCode", this.title);
        map.put("firstName", this.firstName);
        map.put("lastName", this.lastName);
        map.put("addr1", this.addr1);
        map.put("addr2", this.addr2);
        map.put("city", this.city);
        map.put("postalCode", this.postalCode);
        map.put("country", this.country);
        map.put("emailAddress", this.emailAddress);
        return map;
    }


    public static AddressMock copyInstance(AddressMock instance)
    {
        Assert.notNull(instance, "Parameter instance may not be null.");
        AddressMock copy = new AddressMock();
        copy.setAddr1(instance.getAddr1());
        copy.setAddr2(instance.getAddr2());
        copy.setCity(instance.getCity());
        copy.setCountry(instance.getCountry());
        copy.setEmailAddress(instance.getEmailAddress());
        copy.setFirstName(instance.getFirstName());
        copy.setLastName(instance.getLastName());
        copy.setPostalCode(instance.getPostalCode());
        copy.setTitle(instance.getTitle());
        return copy;
    }
}
