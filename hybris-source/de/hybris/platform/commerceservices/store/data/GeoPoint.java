package de.hybris.platform.commerceservices.store.data;

import java.io.Serializable;

public class GeoPoint implements Serializable
{
    private static final long serialVersionUID = 1L;
    private double latitude;
    private double longitude;


    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }


    public double getLatitude()
    {
        return this.latitude;
    }


    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }


    public double getLongitude()
    {
        return this.longitude;
    }
}
