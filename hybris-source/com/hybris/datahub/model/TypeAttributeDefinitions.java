package com.hybris.datahub.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TypeAttributeDefinitions
{
    private final String itemType;
    private final Map<String, DataItemAttribute> attributes;


    public TypeAttributeDefinitions(String type, Collection<DataItemAttribute> attr)
    {
        this.itemType = type;
        this.attributes = map(attr);
    }


    private static Map<String, DataItemAttribute> map(Collection<DataItemAttribute> attributes)
    {
        Map<String, DataItemAttribute> map = new HashMap<>(attributes.size());
        attributes.forEach(attr -> map.put(attr.getPropertyName(), attr));
        return map;
    }


    public String getItemType()
    {
        return this.itemType;
    }


    public Collection<DataItemAttribute> getAll()
    {
        return this.attributes.values();
    }


    public DataItemAttribute getAttribute(String name)
    {
        return this.attributes.get(name);
    }


    public boolean isEmpty()
    {
        return this.attributes.isEmpty();
    }
}
