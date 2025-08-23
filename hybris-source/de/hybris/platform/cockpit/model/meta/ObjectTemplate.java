package de.hybris.platform.cockpit.model.meta;

import java.util.Collection;

public interface ObjectTemplate extends ObjectType
{
    BaseType getBaseType();


    Collection<? extends ExtendedType> getExtendedTypes();


    boolean isDefaultTemplate();
}
