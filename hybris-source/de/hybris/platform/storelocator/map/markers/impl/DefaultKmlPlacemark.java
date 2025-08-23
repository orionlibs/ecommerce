package de.hybris.platform.storelocator.map.markers.impl;

import de.hybris.platform.storelocator.map.markers.KmlDescription;
import de.hybris.platform.storelocator.map.markers.KmlGps;
import de.hybris.platform.storelocator.map.markers.KmlIconStyle;
import de.hybris.platform.storelocator.map.markers.KmlPlacemark;
import de.hybris.platform.storelocator.map.markers.KmlStyle;

public class DefaultKmlPlacemark implements KmlPlacemark
{
    private static String BEGINING = "<Placemark>";
    private static String ENDING = "</Placemark>";
    private final String name;
    private KmlDescription description;
    private final KmlGps gps;
    private KmlIconStyle style;


    public DefaultKmlPlacemark(String name, KmlGps gps, KmlIconStyle style)
    {
        this.name = name;
        this.gps = gps;
        this.style = style;
    }


    public DefaultKmlPlacemark(String name, KmlGps gps)
    {
        this.name = name;
        this.gps = gps;
    }


    public DefaultKmlPlacemark(String name, KmlDescription description, KmlGps gps)
    {
        this.name = name;
        this.description = description;
        this.gps = gps;
    }


    public DefaultKmlPlacemark(String name, KmlDescription description, KmlGps gps, KmlIconStyle style)
    {
        this.name = name;
        this.description = description;
        this.gps = gps;
        this.style = style;
    }


    public KmlDescription getDescription()
    {
        return this.description;
    }


    public KmlGps getGPS()
    {
        return this.gps;
    }


    public String getName()
    {
        return this.name;
    }


    public String getNameElement()
    {
        return "<name>" + this.name + "</name>";
    }


    public KmlStyle getStyle()
    {
        return (KmlStyle)this.style;
    }


    public String getElement()
    {
        StringBuilder builder = new StringBuilder(BEGINING);
        builder.append(getNameElement());
        if(null != this.description)
        {
            builder.append(this.description.getElement());
        }
        builder.append(this.gps.getElement());
        if(this.style != null)
        {
            builder.append(this.style.getPlacemarkInjectionElement());
        }
        builder.append(ENDING);
        return builder.toString();
    }


    public String getBeginning()
    {
        return BEGINING;
    }


    public String getEnding()
    {
        return ENDING;
    }
}
