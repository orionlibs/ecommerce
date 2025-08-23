package de.hybris.platform.cockpit.services.media.impl;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.meta.ObjectTypeFilter;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractObjectTypeFilter<TYPE extends ObjectType, TARGET> implements ObjectTypeFilter<TYPE, TARGET>
{
    public abstract boolean isValidType(TYPE paramTYPE, TARGET paramTARGET);


    public Collection<TYPE> filterObjectTypes(Collection<TYPE> types, TARGET target)
    {
        ServicesUtil.validateParameterNotNull(types, "ObjectTypes collection cannot be null.");
        List<TYPE> result = new LinkedList<>(types);
        for(ObjectType objectType : types)
        {
            if(!isValidType((TYPE)objectType, target))
            {
                result.remove(objectType);
            }
        }
        return result;
    }
}
