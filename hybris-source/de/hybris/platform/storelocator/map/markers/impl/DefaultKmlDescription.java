package de.hybris.platform.storelocator.map.markers.impl;

import de.hybris.platform.storelocator.map.markers.KmlDescription;

public class DefaultKmlDescription implements KmlDescription
{
    private static final String BEGINNING = "<description>";
    private static final String ENDING = "</description>";
    private final String content;


    public DefaultKmlDescription(String description)
    {
        this.content = description;
    }


    public String getBeginning()
    {
        return "<description>";
    }


    public String getElement()
    {
        if(this.content != null)
        {
            return getBeginning() + getBeginning() + this.content;
        }
        return "";
    }


    public String getEnding()
    {
        return "</description>";
    }
}
