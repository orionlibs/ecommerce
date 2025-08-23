package de.hybris.platform.cockpit.model.meta;

import java.util.Set;

public interface RedeclarableObjectType extends ObjectType
{
    Set<PropertyDescriptor> getRedeclaredPropertyDescriptors();
}
