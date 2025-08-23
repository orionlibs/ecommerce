package de.hybris.platform.storelocator.map.markers.impl;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.map.markers.KmlGps;

public class DefaultKmlGps implements KmlGps
{
    private static String BEGINING = "<Point><coordinates>";
    private static String ENDING = "</coordinates></Point>";
    private final GPS gps;


    public DefaultKmlGps(GPS gps)
    {
        this.gps = gps;
    }


    public GPS getGps()
    {
        return this.gps;
    }


    public String getBeginning()
    {
        return BEGINING;
    }


    public String getElement()
    {
        StringBuilder element = new StringBuilder(BEGINING);
        element.append(this.gps.getDecimalLongitude());
        element.append(",");
        element.append(this.gps.getDecimalLatitude());
        element.append(ENDING);
        return element.toString();
    }


    public String getEnding()
    {
        return ENDING;
    }
}
