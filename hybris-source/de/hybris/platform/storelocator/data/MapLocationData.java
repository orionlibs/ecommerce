package de.hybris.platform.storelocator.data;

public class MapLocationData
{
    private String code;
    private String addressDescription;
    private String latitude;
    private String longitude;


    public String getCode()
    {
        return this.code;
    }


    public void setCode(String code)
    {
        this.code = code;
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


    public String getAddressDescription()
    {
        return this.addressDescription;
    }


    public void setAddressDescription(String addressDescription)
    {
        this.addressDescription = addressDescription;
    }
}
