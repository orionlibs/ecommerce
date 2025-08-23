package de.hybris.platform.storelocator.map.markers.impl;

import de.hybris.platform.storelocator.map.markers.KmlStyle;

public class DefaultRouteStyle implements KmlStyle
{
    private final String name;
    private final String element;
    public static final DefaultRouteStyle DEFAULT_STYLE = new DefaultRouteStyle("roadStyle", "<Style id=\"roadStyle\"><LineStyle><color>7fcf0064</color><width>6</width></LineStyle></Style>");


    public DefaultRouteStyle(String name, String element)
    {
        this.element = element;
        this.name = name;
    }


    public String getBeginning()
    {
        return null;
    }


    public String getElement()
    {
        return this.element;
    }


    public String getEnding()
    {
        return null;
    }


    public String getName()
    {
        return this.name;
    }


    public String getPlacemarkInjectionElement()
    {
        return "<styleUrl>#" + this.name + "</styleUrl>";
    }
}
