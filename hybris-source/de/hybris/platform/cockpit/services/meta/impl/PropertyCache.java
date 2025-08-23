package de.hybris.platform.cockpit.services.meta.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.Map;
import org.apache.commons.collections.map.LRUMap;

public class PropertyCache
{
    private final Map<String, PropertyDescriptor> propMap;


    public PropertyCache()
    {
        this(1000);
    }


    public PropertyCache(int maxSize)
    {
        this.propMap = (Map<String, PropertyDescriptor>)new LRUMap(Math.max(1, maxSize));
    }


    public <T extends PropertyDescriptor> T getProperty(String qualifier)
    {
        return (T)this.propMap.get(qualifier.toLowerCase());
    }


    public <T extends PropertyDescriptor> void addProperty(String qualifier, T propertyDescriptor)
    {
        this.propMap.put(qualifier.toLowerCase(), (PropertyDescriptor)propertyDescriptor);
    }


    public void clear()
    {
        this.propMap.clear();
    }
}
