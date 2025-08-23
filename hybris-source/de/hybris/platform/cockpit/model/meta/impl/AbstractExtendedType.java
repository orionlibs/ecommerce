package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractExtendedType implements ExtendedType
{
    public Set<ObjectType> getSupertypes()
    {
        return Collections.EMPTY_SET;
    }


    public Set<ObjectType> getSubtypes()
    {
        return Collections.EMPTY_SET;
    }


    public boolean isAssignableFrom(ObjectType type)
    {
        return (getCode().equals(type.getCode()) || getSubtypes().contains(type));
    }


    public Set<PropertyDescriptor> getPropertyDescriptors()
    {
        Set<PropertyDescriptor> ret = new LinkedHashSet<>();
        for(ObjectType type : getSupertypes())
        {
            Set<PropertyDescriptor> descriptors = type.getDeclaredPropertyDescriptors();
            if(descriptors != null)
            {
                ret.addAll(descriptors);
            }
        }
        ret.addAll(getDeclaredPropertyDescriptors());
        return ret;
    }


    public boolean isAbstract()
    {
        return false;
    }


    public String getDescription()
    {
        return null;
    }


    public String getDescription(String languageIsoCode)
    {
        return null;
    }
}
