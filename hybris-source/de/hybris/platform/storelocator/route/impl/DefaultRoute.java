package de.hybris.platform.storelocator.route.impl;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.map.markers.KmlStyle;
import de.hybris.platform.storelocator.map.markers.impl.DefaultRouteStyle;
import de.hybris.platform.storelocator.route.Route;

public class DefaultRoute implements Route
{
    private static final String BEGINING = "<Placemark><name>Route</name>";
    private static final String ENDING = "</Placemark>";
    private final String kmlElement;
    private final GPS start;
    private final Location destination;
    private final KmlStyle style = (KmlStyle)DefaultRouteStyle.DEFAULT_STYLE;


    public DefaultRoute(GPS start, Location destination, String kmlElement)
    {
        this.kmlElement = kmlElement;
        this.start = start;
        this.destination = destination;
    }


    public String getCoordinates()
    {
        return this.kmlElement;
    }


    public String getBeginning()
    {
        return "<Placemark><name>Route</name>";
    }


    public String getElement()
    {
        return "<Placemark><name>Route</name>" + this.kmlElement + this.style.getPlacemarkInjectionElement() + "</Placemark>";
    }


    public String getEnding()
    {
        return "</Placemark>";
    }


    public KmlStyle getRouteStyle()
    {
        return this.style;
    }


    public GPS getStart()
    {
        return this.start;
    }


    public Location getDestination()
    {
        return this.destination;
    }
}
