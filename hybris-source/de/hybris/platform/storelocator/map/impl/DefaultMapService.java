package de.hybris.platform.storelocator.map.impl;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.exception.GoogleMapException;
import de.hybris.platform.storelocator.exception.MapServiceException;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.map.Map;
import de.hybris.platform.storelocator.map.MapService;
import de.hybris.platform.storelocator.map.utils.MapBounds;
import de.hybris.platform.storelocator.route.RouteService;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMapService implements MapService
{
    public static final double DEFAULT_RADIUS = 5.0D;
    private RouteService routeService;


    public Map getMap(GPS gps, String title)
    {
        return getMap(gps, title, 5.0D);
    }


    public Map getMap(GPS gps, String title, double radius)
    {
        try
        {
            return (Map)DefaultMap.create(gps, radius, title);
        }
        catch(GoogleMapException e)
        {
            throw new MapServiceException("Could not get map due to " + e.getMessage(), e);
        }
    }


    public Map getMap(GPS gps, double radius, List<Location> poi, String title)
    {
        try
        {
            return (Map)DefaultMap.create(gps, radius, title, poi);
        }
        catch(GoogleMapException e)
        {
            throw new MapServiceException("Could not get map due to " + e.getMessage(), e);
        }
    }


    public Map getMap(GPS center, double radius, List<Location> poi, String title, Location routeTo)
    {
        try
        {
            return (Map)DefaultMap.create(center, radius, title, poi, getRouteService().getDistanceAndRoute(center, routeTo));
        }
        catch(GoogleMapException | de.hybris.platform.storelocator.exception.RouteServiceException e)
        {
            throw new MapServiceException(e);
        }
    }


    public MapBounds getMapBoundsForMap(Map map)
    {
        if(map != null && CollectionUtils.isNotEmpty(map.getPointsOfInterest()))
        {
            GPS northEast = ((Location)map.getPointsOfInterest().get(0)).getGPS();
            GPS southWest = ((Location)map.getPointsOfInterest().get(0)).getGPS();
            for(Location location : map.getPointsOfInterest())
            {
                northEast = determineNorthEastCorner(northEast, location);
                southWest = determineSouthWestCorner(southWest, location);
            }
            return recalculateBoundsAgainstMapCenter(southWest, northEast, map.getGps());
        }
        return null;
    }


    protected GPS determineSouthWestCorner(GPS southWest, Location location)
    {
        double maxSouth = Math.min(location.getGPS().getDecimalLatitude(), southWest.getDecimalLatitude());
        double maxWest = Math.min(location.getGPS().getDecimalLongitude(), southWest.getDecimalLongitude());
        return southWest.create(maxSouth, maxWest);
    }


    protected GPS determineNorthEastCorner(GPS northEast, Location location)
    {
        double maxNorth = Math.max(location.getGPS().getDecimalLatitude(), northEast.getDecimalLatitude());
        double maxEast = Math.max(location.getGPS().getDecimalLongitude(), northEast.getDecimalLongitude());
        return northEast.create(maxNorth, maxEast);
    }


    protected MapBounds recalculateBoundsAgainstMapCenter(GPS southWest, GPS northEast, GPS centerPosition)
    {
        double maxE, maxW, maxN, maxS, wSpan = southWest.getDecimalLongitude() - centerPosition.getDecimalLongitude();
        double eSpan = northEast.getDecimalLongitude() - centerPosition.getDecimalLongitude();
        double sSpan = southWest.getDecimalLatitude() - centerPosition.getDecimalLatitude();
        double nSpan = northEast.getDecimalLatitude() - centerPosition.getDecimalLatitude();
        if(Math.abs(eSpan) > Math.abs(wSpan))
        {
            maxW = centerPosition.getDecimalLongitude() - Math.abs(eSpan);
            maxE = centerPosition.getDecimalLongitude() + Math.abs(eSpan);
        }
        else
        {
            maxE = centerPosition.getDecimalLongitude() + Math.abs(wSpan);
            maxW = centerPosition.getDecimalLongitude() - Math.abs(wSpan);
        }
        if(Math.abs(sSpan) > Math.abs(nSpan))
        {
            maxN = centerPosition.getDecimalLatitude() + Math.abs(sSpan);
            maxS = centerPosition.getDecimalLatitude() - Math.abs(sSpan);
        }
        else
        {
            maxS = centerPosition.getDecimalLatitude() - Math.abs(nSpan);
            maxN = centerPosition.getDecimalLatitude() + Math.abs(nSpan);
        }
        return new MapBounds(northEast.create(maxN, maxE), southWest.create(maxS, maxW));
    }


    protected RouteService getRouteService()
    {
        return this.routeService;
    }


    @Required
    public void setRouteService(RouteService routeService)
    {
        this.routeService = routeService;
    }
}
