package de.hybris.platform.cockpit.model.meta;

import java.util.Collection;

public interface TypedObject
{
    BaseType getType();


    Collection<ExtendedType> getExtendedTypes();


    Collection<ObjectTemplate> getPotentialTemplates();


    Collection<ObjectTemplate> getAssignedTemplates();


    Object getObject();


    boolean instanceOf(ObjectType paramObjectType);
}
