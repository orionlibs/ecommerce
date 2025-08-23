package de.hybris.platform.storelocator.location.impl;

import java.io.Serializable;

public class LocationDTO implements Serializable
{
    public static final String LOCATION_TYPE_STORE = "STORE";
    public static final String LOCATION_TYPE_WAREHOUSE = "WAREHOUSE";
    public static final String LOCATION_TYPE_POS = "POS";
    private String name;
    private String description;
    private String street;
    private String buildingNo;
    private String postalCode;
    private String city;
    private String countryIsoCode;
    private String mapIconUrl;
    private String latitude;
    private String longitude;
    private String type;


    public LocationDTO()
    {
    }


    public LocationDTO(String street, String buildingNo, String postalCode, String city, String countryIsoCode)
    {
        this.street = street;
        this.buildingNo = buildingNo;
        this.postalCode = postalCode;
        this.city = city;
        this.countryIsoCode = countryIsoCode;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getStreet()
    {
        return this.street;
    }


    public void setStreet(String street)
    {
        this.street = street;
    }


    public String getBuildingNo()
    {
        return this.buildingNo;
    }


    public void setBuildingNo(String buildingNo)
    {
        this.buildingNo = buildingNo;
    }


    public String getPostalCode()
    {
        return this.postalCode;
    }


    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }


    public String getCity()
    {
        return this.city;
    }


    public void setCity(String city)
    {
        this.city = city;
    }


    public String getCountryIsoCode()
    {
        return this.countryIsoCode;
    }


    public void setCountryIsoCode(String countryIsoCode)
    {
        this.countryIsoCode = countryIsoCode;
    }


    public String getMapIconUrl()
    {
        return this.mapIconUrl;
    }


    public void setMapIconUrl(String mapIconUrl)
    {
        this.mapIconUrl = mapIconUrl;
    }


    public String getLatitude()
    {
        return this.latitude;
    }


    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }


    public String getLongitude()
    {
        return this.longitude;
    }


    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String type)
    {
        this.type = type;
    }
}
