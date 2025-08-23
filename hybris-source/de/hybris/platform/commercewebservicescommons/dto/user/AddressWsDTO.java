package de.hybris.platform.commercewebservicescommons.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Address", description = "Request body fields required and optional to operate on address data. The DTO is in XML or .json format")
public class AddressWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "id", value = "Unique id value of the address which is optional while creating new address. While performing other address operations this value is the key")
    private String id;
    @ApiModelProperty(name = "title", value = "Title of the address person")
    private String title;
    @ApiModelProperty(name = "titleCode", value = "Code of the title", required = true)
    private String titleCode;
    @ApiModelProperty(name = "firstName", value = "First name of the address person", required = true)
    private String firstName;
    @ApiModelProperty(name = "lastName", value = "Last name of the address person", required = true)
    private String lastName;
    @ApiModelProperty(name = "companyName", value = "Company Name")
    private String companyName;
    @ApiModelProperty(name = "line1", value = "First line of the address", required = true)
    private String line1;
    @ApiModelProperty(name = "line2", value = "Second line of the address")
    private String line2;
    @ApiModelProperty(name = "town", value = "Town, field required", required = true)
    private String town;
    @ApiModelProperty(name = "region", value = "Region where address belongs to")
    private RegionWsDTO region;
    @ApiModelProperty(name = "district", value = "District name")
    private String district;
    @ApiModelProperty(name = "postalCode", value = "Postal code of the address", required = true)
    private String postalCode;
    @ApiModelProperty(name = "phone", value = "Phone number")
    private String phone;
    @ApiModelProperty(name = "cellphone", value = "Cellphone number")
    private String cellphone;
    @ApiModelProperty(name = "email", value = "Email address")
    private String email;
    @ApiModelProperty(name = "country", value = "Country where address is located")
    private CountryWsDTO country;
    @ApiModelProperty(name = "shippingAddress", value = "Boolean flag if address is for shipping")
    private Boolean shippingAddress;
    @ApiModelProperty(name = "defaultAddress", value = "Boolean flag if address is default")
    private Boolean defaultAddress;
    @ApiModelProperty(name = "visibleInAddressBook", value = "Boolean flag if address is visible in the Address Book")
    private Boolean visibleInAddressBook;
    @ApiModelProperty(name = "formattedAddress", value = "Boolean flag if address is formatted")
    private String formattedAddress;


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


    public void setRegion(RegionWsDTO region)
    {
        this.region = region;
    }


    public RegionWsDTO getRegion()
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


    public void setCountry(CountryWsDTO country)
    {
        this.country = country;
    }


    public CountryWsDTO getCountry()
    {
        return this.country;
    }


    public void setShippingAddress(Boolean shippingAddress)
    {
        this.shippingAddress = shippingAddress;
    }


    public Boolean getShippingAddress()
    {
        return this.shippingAddress;
    }


    public void setDefaultAddress(Boolean defaultAddress)
    {
        this.defaultAddress = defaultAddress;
    }


    public Boolean getDefaultAddress()
    {
        return this.defaultAddress;
    }


    public void setVisibleInAddressBook(Boolean visibleInAddressBook)
    {
        this.visibleInAddressBook = visibleInAddressBook;
    }


    public Boolean getVisibleInAddressBook()
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
}
