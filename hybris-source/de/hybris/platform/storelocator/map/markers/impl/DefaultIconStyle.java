package de.hybris.platform.storelocator.map.markers.impl;

import de.hybris.platform.storelocator.map.markers.KmlIconStyle;

public class DefaultIconStyle implements KmlIconStyle
{
    private final String name;
    private final String iconHref;


    public DefaultIconStyle(String name, String iconHref)
    {
        this.name = name;
        this.iconHref = iconHref;
    }


    public String getIconHref()
    {
        return this.iconHref;
    }


    public String getPlacemarkInjectionElement()
    {
        return "<styleUrl>#" + this.name + "</styleUrl>";
    }


    public String getBeginning()
    {
        return null;
    }


    public String getElement()
    {
        return "<Style id=\"" + this.name + "\"><IconStyle><colorMode>normal</colorMode><scale>1</scale><Icon><href>" + this.iconHref + "</href></Icon></IconStyle></Style>";
    }


    public String getEnding()
    {
        return null;
    }
}
