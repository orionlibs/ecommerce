package de.hybris.platform.storelocator.data;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import java.io.Serializable;
import java.util.Objects;

public class AddressData implements Serializable
{
    private String name;
    private String street;
    private String building;
    private String zip;
    private String city;
    private String countryCode;


    public AddressData()
    {
    }


    public AddressData(String zip)
    {
        this.zip = zip;
    }


    public AddressData(String street, String buildingNo, String zip, String city, String countryCode)
    {
        this.street = street;
        this.building = buildingNo;
        this.zip = zip;
        this.city = city;
        this.countryCode = countryCode;
    }


    public AddressData(String name, String street, String buildingNo, String zip, String city, String countryCode)
    {
        this.name = name;
        this.street = street;
        this.building = buildingNo;
        this.zip = zip;
        this.city = city;
        this.countryCode = countryCode;
    }


    public AddressData(AddressModel addressModel)
    {
        if(addressModel != null)
        {
            this.street = addressModel.getStreetname();
            this.building = addressModel.getStreetnumber();
            this.zip = addressModel.getPostalcode();
            this.city = addressModel.getTown();
            CountryModel country = addressModel.getCountry();
            if(country != null)
            {
                this.countryCode = country.getIsocode();
            }
        }
    }


    public String getName()
    {
        return this.name;
    }


    public String getZip()
    {
        return this.zip;
    }


    public void setZip(String zip)
    {
        this.zip = zip;
    }


    public String getCity()
    {
        return this.city;
    }


    public void setCity(String city)
    {
        this.city = city;
    }


    public String getCountryCode()
    {
        return this.countryCode;
    }


    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }


    public String getStreet()
    {
        return this.street;
    }


    public String getBuilding()
    {
        return this.building;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setStreet(String street)
    {
        this.street = street;
    }


    public void setBuilding(String building)
    {
        this.building = building;
    }


    public String toString()
    {
        StringBuilder builder = new StringBuilder((this.name == null) ? "" : this.name);
        builder.append(", ").append(this.street).append(" ").append(this.building).append(", ").append(this.zip).append(" ").append(this.city);
        return builder.toString();
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(obj.getClass().equals(AddressData.class))
        {
            AddressData address = (AddressData)obj;
            return (equalAddressFields(this.street, address.street) && equalAddressFields(this.building, address.building) &&
                            equalAddressFields(this.zip, address.zip) && equalAddressFields(this.city, address.city) &&
                            equalAddressFields(this.countryCode, address.countryCode));
        }
        if(obj.getClass().equals(AddressModel.class))
        {
            AddressModel address = (AddressModel)obj;
            return addressEquals(address);
        }
        return false;
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.building, this.city, this.countryCode, this.street, this.zip});
    }


    public boolean addressEquals(AddressModel addressModel)
    {
        if(addressModel != null)
        {
            boolean result = equalAddressFields(this.street, addressModel.getStreetname());
            if(!result)
            {
                return result;
            }
            result = equalAddressFields(this.building, addressModel.getStreetnumber());
            if(!result)
            {
                return result;
            }
            result = equalAddressFields(this.zip, addressModel.getPostalcode());
            if(!result)
            {
                return result;
            }
            result = equalAddressFields(this.city, addressModel.getTown());
            if(!result)
            {
                return result;
            }
            result = equalAddressFields(this.countryCode,
                            (addressModel.getCountry() == null) ? null : addressModel.getCountry().getIsocode());
            if(!result)
            {
                return result;
            }
            return result;
        }
        return false;
    }


    private boolean equalAddressFields(String field1, String field2)
    {
        if((((field1 == null) ? 1 : 0) ^ ((field2 == null) ? 1 : 0)) != 0)
        {
            return false;
        }
        if(field1 != null)
        {
            return field1.equalsIgnoreCase(field2);
        }
        return true;
    }
}
