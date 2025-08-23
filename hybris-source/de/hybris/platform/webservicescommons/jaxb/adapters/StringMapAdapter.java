package de.hybris.platform.webservicescommons.jaxb.adapters;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringMapAdapter extends XmlAdapter<StringMapAdapter.MapContainer, Map<String, String>>
{
    public Map<String, String> unmarshal(MapContainer v)
    {
        if(v == null)
        {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for(MapElement element : v.getEntry())
        {
            result.put(element.key, element.value);
        }
        return result;
    }


    public MapContainer marshal(Map<String, String> v)
    {
        if(v == null)
        {
            return null;
        }
        MapContainer result = new MapContainer();
        for(Map.Entry<String, String> entry : v.entrySet())
        {
            result.getEntry().add(new MapElement(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
