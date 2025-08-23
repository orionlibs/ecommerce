package de.hybris.platform.cockpit.model.meta;

import java.util.Set;

public interface ObjectType
{
    String getCode();


    Set<ObjectType> getSupertypes();


    Set<ObjectType> getSubtypes();


    boolean isAssignableFrom(ObjectType paramObjectType);


    Set<PropertyDescriptor> getPropertyDescriptors();


    Set<PropertyDescriptor> getDeclaredPropertyDescriptors();


    boolean isAbstract();


    String getName();


    String getName(String paramString);


    String getDescription();


    String getDescription(String paramString);
}
