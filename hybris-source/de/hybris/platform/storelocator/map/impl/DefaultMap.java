package de.hybris.platform.storelocator.map.impl;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.GoogleMapException;
import de.hybris.platform.storelocator.exception.KmlDocumentException;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.map.Map;
import de.hybris.platform.storelocator.map.markers.KmlDocument;
import de.hybris.platform.storelocator.map.markers.impl.DefaultKmlDocument;
import de.hybris.platform.storelocator.map.utils.MapBounds;
import de.hybris.platform.storelocator.route.DistanceAndRoute;
import java.util.List;
import org.apache.log4j.Logger;

public class DefaultMap implements Map
{
    private static final Logger LOG = Logger.getLogger(DefaultMap.class);
    private final GPS gps;
    private final double radius;
    private final KmlDocument kml;
    private final List<Location> pois;
    private final String title;
    private final DistanceAndRoute route;
    private MapBounds mapBounds;


    public DefaultMap(GPS gps, double radius, List<Location> pois, DistanceAndRoute route, String title, KmlDocument kml)
    {
        this.gps = gps;
        this.radius = radius;
        this.pois = pois;
        this.title = title;
        this.kml = kml;
        this.route = route;
        try
        {
            this.mapBounds = new MapBounds(gps, radius);
        }
        catch(GeoLocatorException e)
        {
            LOG.warn("Could not create map bounds due to : " + e.getMessage(), (Throwable)e);
        }
    }


    public static DefaultMap create(GPS center, double radius, String title) throws GoogleMapException
    {
        return new DefaultMap(center, radius, null, null, title, null);
    }


    public static DefaultMap create(GPS center, double radius, String title, List<Location> poi) throws GoogleMapException
    {
        return create(center, radius, title, poi, null);
    }


    public static DefaultMap create(GPS center, double radius, String title, List<Location> poi, DistanceAndRoute route) throws GoogleMapException
    {
        try
        {
            DefaultKmlDocument defaultKmlDocument = new DefaultKmlDocument(center, poi, route);
            return new DefaultMap(center, radius, poi, route, title, (KmlDocument)defaultKmlDocument);
        }
        catch(KmlDocumentException e)
        {
            throw new GoogleMapException("Could not create Google Map", e);
        }
    }


    public GPS getGps()
    {
        return this.gps;
    }


    public List<Location> getPointsOfInterest()
    {
        return this.pois;
    }


    public double getRadius()
    {
        return this.radius;
    }


    public String getTitle()
    {
        return this.title;
    }


    public KmlDocument getKml()
    {
        return this.kml;
    }


    public DistanceAndRoute getDistanceAndRoute()
    {
        return this.route;
    }


    public MapBounds getMapBounds()
    {
        return this.mapBounds;
    }
}
