package de.hybris.platform.cockpit.model.config;

import de.hybris.platform.cockpit.model.meta.ObjectType;

public class PropertyGroupTypeConfiguration extends PropertyGroupConfiguration
{
    private final ObjectType type;


    public PropertyGroupTypeConfiguration(ObjectType type)
    {
        if(type == null)
        {
            throw new IllegalArgumentException("type was null");
        }
        this.type = type;
    }


    public ObjectType getType()
    {
        return this.type;
    }
}
