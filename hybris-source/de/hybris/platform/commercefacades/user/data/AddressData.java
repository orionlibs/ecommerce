package de.hybris.platform.commercefacades.user.data;

import java.io.Serializable;

public class AddressData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String titleCode;
    private String firstName;
    private String lastName;
    private String companyName;
    private String line1;
    private String line2;
    private String town;
    private RegionData region;
    private String district;
    private String postalCode;
    private String phone;
    private String cellphone;
    private String email;
    private CountryData country;
    private boolean shippingAddress;
    private boolean billingAddress;
    private boolean defaultAddress;
    private boolean visibleInAddressBook;
    private String formattedAddress;
    private boolean editable;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getTitle()
    {
        return this.title;
    }


    public void setTitleCode(String titleCode)
    {
        this.titleCode = titleCode;
    }


    public String getTitleCode()
    {
        return this.titleCode;
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


    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }


    public String getCompanyName()
    {
        return this.companyName;
    }


    public void setLine1(String line1)
    {
        this.line1 = line1;
    }


    public String getLine1()
    {
        return this.line1;
    }


    public void setLine2(String line2)
    {
        this.line2 = line2;
    }


    public String getLine2()
    {
        return this.line2;
    }


    public void setTown(String town)
    {
        this.town = town;
    }


    public String getTown()
    {
        return this.town;
    }


    public void setRegion(RegionData region)
    {
        this.region = region;
    }


    public RegionData getRegion()
    {
        return this.region;
    }


    public void setDistrict(String district)
    {
        this.district = district;
    }


    public String getDistrict()
    {
        return this.district;
    }


    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }


    public String getPostalCode()
    {
        return this.postalCode;
    }


    public void setPhone(String phone)
    {
        this.phone = phone;
    }


    public String getPhone()
    {
        return this.phone;
    }


    public void setCellphone(String cellphone)
    {
        this.cellphone = cellphone;
    }


    public String getCellphone()
    {
        return this.cellphone;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getEmail()
    {
        return this.email;
    }


    public void setCountry(CountryData country)
    {
        this.country = country;
    }


    public CountryData getCountry()
    {
        return this.country;
    }


    public void setShippingAddress(boolean shippingAddress)
    {
        this.shippingAddress = shippingAddress;
    }


    public boolean isShippingAddress()
    {
        return this.shippingAddress;
    }


    public void setBillingAddress(boolean billingAddress)
    {
        this.billingAddress = billingAddress;
    }


    public boolean isBillingAddress()
    {
        return this.billingAddress;
    }


    public void setDefaultAddress(boolean defaultAddress)
    {
        this.defaultAddress = defaultAddress;
    }


    public boolean isDefaultAddress()
    {
        return this.defaultAddress;
    }


    public void setVisibleInAddressBook(boolean visibleInAddressBook)
    {
        this.visibleInAddressBook = visibleInAddressBook;
    }


    public boolean isVisibleInAddressBook()
    {
        return this.visibleInAddressBook;
    }


    public void setFormattedAddress(String formattedAddress)
    {
        this.formattedAddress = formattedAddress;
    }


    public String getFormattedAddress()
    {
        return this.formattedAddress;
    }


    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }


    public boolean isEditable()
    {
        return this.editable;
    }
}
