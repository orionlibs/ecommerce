package de.hybris.platform.cockpit.services.meta.impl;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import java.util.Map;
import org.apache.commons.collections.map.LRUMap;

public class TypeCache
{
    private final Map<String, ObjectType> typeMap;


    public TypeCache()
    {
        this(100);
    }


    public TypeCache(int maxSize)
    {
        this.typeMap = (Map<String, ObjectType>)new LRUMap(Math.max(1, maxSize));
    }


    public <T extends ObjectType> T getType(String code)
    {
        return (T)this.typeMap.get(code.toLowerCase());
    }


    public <T extends ObjectType> void addType(String code, T type)
    {
        this.typeMap.put(code.toLowerCase(), (ObjectType)type);
    }


    public ObjectType removeType(String code)
    {
        return this.typeMap.remove(code.toLowerCase());
    }


    public void clear()
    {
        this.typeMap.clear();
    }
}
