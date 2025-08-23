package de.hybris.platform.storelocator.route;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.exception.RouteServiceException;
import de.hybris.platform.storelocator.location.Location;

public interface RouteService
{
    DistanceAndRoute getDistanceAndRoute(Location paramLocation1, Location paramLocation2) throws RouteServiceException;


    DistanceAndRoute getDistanceAndRoute(GPS paramGPS, Location paramLocation) throws RouteServiceException;
}
