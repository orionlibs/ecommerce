package de.hybris.platform.commerceservices.storefinder.data;

import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.io.Serializable;

public class PointOfServiceDistanceData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private PointOfServiceModel pointOfService;
    private double distanceKm;


    public void setPointOfService(PointOfServiceModel pointOfService)
    {
        this.pointOfService = pointOfService;
    }


    public PointOfServiceModel getPointOfService()
    {
        return this.pointOfService;
    }


    public void setDistanceKm(double distanceKm)
    {
        this.distanceKm = distanceKm;
    }


    public double getDistanceKm()
    {
        return this.distanceKm;
    }
}
