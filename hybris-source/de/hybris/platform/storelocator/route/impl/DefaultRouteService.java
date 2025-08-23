package de.hybris.platform.storelocator.route.impl;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.exception.RouteServiceException;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.route.DistanceAndRoute;
import de.hybris.platform.storelocator.route.RouteService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRouteService implements RouteService
{
    private GeoWebServiceWrapper geoServiceWrapper;


    public DistanceAndRoute getDistanceAndRoute(Location start, Location dest)
    {
        try
        {
            return getGeoServiceWrapper().getDistanceAndRoute(start, dest);
        }
        catch(GeoServiceWrapperException e)
        {
            throw new RouteServiceException(e);
        }
    }


    public DistanceAndRoute getDistanceAndRoute(GPS start, Location dest)
    {
        try
        {
            return getGeoServiceWrapper().getDistanceAndRoute(start, dest);
        }
        catch(GeoServiceWrapperException e)
        {
            throw new RouteServiceException(e);
        }
    }


    protected GeoWebServiceWrapper getGeoServiceWrapper()
    {
        return this.geoServiceWrapper;
    }


    @Required
    public void setGeoServiceWrapper(GeoWebServiceWrapper geoServiceWrapper)
    {
        this.geoServiceWrapper = geoServiceWrapper;
    }
}
