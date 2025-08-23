package de.hybris.platform.storelocator.map;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.MapServiceException;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.map.utils.MapBounds;
import java.util.List;

public interface MapService
{
    Map getMap(GPS paramGPS, String paramString, double paramDouble) throws MapServiceException;


    Map getMap(GPS paramGPS, String paramString) throws MapServiceException;


    Map getMap(GPS paramGPS, double paramDouble, List<Location> paramList, String paramString) throws MapServiceException;


    Map getMap(GPS paramGPS, double paramDouble, List<Location> paramList, String paramString, Location paramLocation) throws MapServiceException;


    MapBounds getMapBoundsForMap(Map paramMap) throws GeoLocatorException;
}
