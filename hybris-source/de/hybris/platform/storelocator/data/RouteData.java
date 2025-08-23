package de.hybris.platform.storelocator.data;

import java.io.Serializable;

public class RouteData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String coordinates;
    private double distance;
    private String distanceText;
    private double duration;
    private String durationText;
    private double eagleFliesDistance;
    private String mode;


    public void setCoordinates(String coordinates)
    {
        this.coordinates = coordinates;
    }


    public String getCoordinates()
    {
        return this.coordinates;
    }


    public void setDistance(double distance)
    {
        this.distance = distance;
    }


    public double getDistance()
    {
        return this.distance;
    }


    public void setDistanceText(String distanceText)
    {
        this.distanceText = distanceText;
    }


    public String getDistanceText()
    {
        return this.distanceText;
    }


    public void setDuration(double duration)
    {
        this.duration = duration;
    }


    public double getDuration()
    {
        return this.duration;
    }


    public void setDurationText(String durationText)
    {
        this.durationText = durationText;
    }


    public String getDurationText()
    {
        return this.durationText;
    }


    public void setEagleFliesDistance(double eagleFliesDistance)
    {
        this.eagleFliesDistance = eagleFliesDistance;
    }


    public double getEagleFliesDistance()
    {
        return this.eagleFliesDistance;
    }


    public void setMode(String mode)
    {
        this.mode = mode;
    }


    public String getMode()
    {
        return this.mode;
    }
}
