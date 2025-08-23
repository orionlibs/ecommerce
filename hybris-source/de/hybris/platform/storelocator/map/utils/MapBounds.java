package de.hybris.platform.storelocator.map.utils;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.impl.GeometryUtils;
import java.util.List;

public class MapBounds
{
    private final GPS northEast;
    private final GPS southWest;


    public MapBounds(GPS center, double radius) throws GeoLocatorException
    {
        List<GPS> corners = GeometryUtils.getSquareOfTolerance(center, radius);
        this.southWest = corners.get(0);
        this.northEast = corners.get(1);
    }


    public MapBounds(GPS northEast, GPS southWest) throws GeoLocatorException
    {
        this.southWest = southWest;
        this.northEast = northEast;
    }


    public GPS getNorthEast()
    {
        return this.northEast;
    }


    public GPS getSouthWest()
    {
        return this.southWest;
    }
}
