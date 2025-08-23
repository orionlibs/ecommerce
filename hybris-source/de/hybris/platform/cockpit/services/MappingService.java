package de.hybris.platform.cockpit.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MappingService
{
    public static Map<Object, Object> mapItems(KeyProvider keyProvider, Collection<? extends Object> items) throws IllegalArgumentException
    {
        Map<Object, Object> ret = new HashMap<>();
        for(Object item : items)
        {
            Object key = keyProvider.getKey(item);
            if(key != null)
            {
                ret.put(key, item);
                continue;
            }
            throw new IllegalArgumentException("Could not retrieve valid key for item '" + item + "'.");
        }
        return ret;
    }
}
