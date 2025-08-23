package de.hybris.platform.storelocator.impl;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.constants.GeolocationMaths;
import de.hybris.platform.storelocator.constants.GeolocationUtils;
import java.text.DecimalFormat;

public class DefaultGPS implements GPS
{
    private double latitude;
    private double longitude;
    private int[] latitudeDMS;
    private int[] longitudeDMS;
    static final long serialVersionUID = 999153875285740630L;


    public DefaultGPS()
    {
    }


    public DefaultGPS(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public DefaultGPS(int[] latitudeDMS, int[] longitudeDMS)
    {
        this.latitudeDMS = latitudeDMS;
        this.longitudeDMS = longitudeDMS;
    }


    public GPS create(String latitude, String longitude)
    {
        if(GeolocationUtils.validateDMSLatitude(latitude) && GeolocationUtils.validateDMSLongitude(longitude))
        {
            DefaultGPS gps = new DefaultGPS(GeolocationUtils.separateDMS(latitude), GeolocationUtils.separateDMS(longitude));
            gps.latitude = GeolocationMaths.dms2Decimal(gps.latitudeDMS);
            gps.longitude = GeolocationMaths.dms2Decimal(gps.longitudeDMS);
            return gps;
        }
        return null;
    }


    public GPS create(double latitude, double longitude)
    {
        if(GeolocationUtils.validateLatitude(latitude) && GeolocationUtils.validateLongitude(longitude))
        {
            DefaultGPS gps = new DefaultGPS(latitude, longitude);
            gps.latitudeDMS = GeolocationMaths.decimal2DMS(latitude);
            gps.longitudeDMS = GeolocationMaths.decimal2DMS(longitude);
            return gps;
        }
        return null;
    }


    public double getDecimalLatitude()
    {
        return this.latitude;
    }


    public double getDecimalLongitude()
    {
        return this.longitude;
    }


    public String toDMSString()
    {
        return String.format("(%1$d°%2$d'%3$d\"%4$s, %5$d°%6$d'%7$d\"%8$s)", new Object[] {Integer.valueOf(Math.abs(this.latitudeDMS[0])), Integer.valueOf(this.latitudeDMS[1]),
                        Integer.valueOf(this.latitudeDMS[2]), (this.latitudeDMS[0] > 0) ? "N" : "S",
                        Integer.valueOf(Math.abs(this.longitudeDMS[0])), Integer.valueOf(this.longitudeDMS[1]),
                        Integer.valueOf(this.longitudeDMS[2]), (this.longitudeDMS[0] > 0) ? "E" : "W"});
    }


    public String toString()
    {
        DecimalFormat format = new DecimalFormat(".######");
        return "(" + format.format(this.latitude) + ", " + format.format(this.longitude) + ")";
    }


    public String toGeocodeServiceFormat()
    {
        DecimalFormat format = new DecimalFormat(".######");
        return format.format(this.latitude).replace(',', '.') + ", " + format.format(this.latitude).replace(',', '.');
    }
}
