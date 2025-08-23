/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.type;

import com.hybris.cockpitng.core.util.CockpitTypeUtils;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class BackofficeTypeUtils implements CockpitTypeUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeTypeUtils.class);
    private TypeFacade typeFacade;


    @Override
    public boolean isAssignableFrom(final String superType, final String subType)
    {
        boolean assignable;
        try
        {
            final DataType sourceDataType = getTypeFacade().load(subType);
            final DataType targetDataType = getTypeFacade().load(superType);
            final boolean typesExist = sourceDataType != null && targetDataType != null;
            final boolean stronglyTyped = typesExist && sourceDataType.getClazz() != null && targetDataType.getClazz() != null;
            assignable = stronglyTyped && targetDataType.getClazz().isAssignableFrom(sourceDataType.getClazz());
        }
        catch(final TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Type not found", e);
            }
            assignable = Objects.equals(subType, superType);
        }
        return assignable;
    }


    @Override
    public String findClosestSuperType(final List<Object> entities)
    {
        if(CollectionUtils.isNotEmpty(entities))
        {
            final Object first = entities.get(0);
            final List<String> supertypes = findSupertypes(first);
            supertypes.add(0, getTypeFacade().getType(first));
            entities.subList(1, entities.size()).forEach(entity -> {
                final List<String> elementsTypes = findSupertypes(entity);
                elementsTypes.add(0, getTypeFacade().getType(entity));
                supertypes.removeIf(type -> !elementsTypes.contains(type));
            });
            if(supertypes.isEmpty())
            {
                LOG.warn("Could not find common supertype");
            }
            else
            {
                return supertypes.get(0);
            }
        }
        return CockpitTypeUtils.super.findClosestSuperType(entities);
    }


    protected List<String> findSupertypes(final Object entity)
    {
        if(entity != null)
        {
            try
            {
                final String type = getTypeFacade().getType(entity);
                final DataType dataType = getTypeFacade().load(type);
                return new LinkedList<>(dataType.getAllSuperTypes());
            }
            catch(final TypeNotFoundException e)
            {
                LOG.debug("Type not found", e);
            }
        }
        final List<String> result = new LinkedList<>();
        result.add(Object.class.getCanonicalName());
        return result;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }
}
