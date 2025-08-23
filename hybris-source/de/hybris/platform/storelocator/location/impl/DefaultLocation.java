package de.hybris.platform.storelocator.location.impl;

import de.hybris.platform.storelocator.location.DistanceAwareLocation;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

public class DefaultLocation extends DistanceUnawareLocation implements DistanceAwareLocation
{
    private final Double distance;


    public DefaultLocation(PointOfServiceModel posModel, Double distance)
    {
        super(posModel);
        if(distance == null)
        {
            throw new IllegalArgumentException("Provided distance should be not null");
        }
        this.distance = distance;
    }


    public Double getDistance()
    {
        return this.distance;
    }


    public int compareTo(DistanceAwareLocation distanceAwareLocation)
    {
        return getDistance().compareTo(distanceAwareLocation.getDistance());
    }
}
