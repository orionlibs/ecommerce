/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.type.impl;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.common.impl.AbstractStrategyRegistry;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link TypeFacade} using a {@link AbstractStrategyRegistry} for delegating to
 * {@link TypeFacadeStrategy}s which then do the actual work.
 */
public class DefaultTypeFacade implements TypeFacade
{
    private TypeFacadeStrategyRegistry strategyRegistry;


    @Override
    public DataType load(final String qualifier, final Context ctx) throws TypeNotFoundException
    {
        final TypeFacadeStrategy strategy = strategyRegistry.getStrategy(qualifier);
        if(strategy == null)
        {
            throw new TypeNotFoundException(qualifier);
        }
        return strategy.load(qualifier, ctx);
    }


    @Override
    public DataType load(final String qualifier) throws TypeNotFoundException
    {
        return this.load(qualifier, null);
    }


    @Required
    public void setStrategyRegistry(final TypeFacadeStrategyRegistry strategyRegistry)
    {
        this.strategyRegistry = strategyRegistry;
    }


    @Override
    public String getType(final Object object)
    {
        Validate.notNull("Object cannot be null", object);
        String typeCode = object.getClass().getName();
        final TypeFacadeStrategy strategy = strategyRegistry.getStrategy(object.getClass().getName());
        if(strategy != null)
        {
            typeCode = strategy.getType(object);
        }
        return typeCode;
    }


    @Override
    public String getAttributeDescription(final String type, final String attribute)
    {
        return getAttributeDescription(type, attribute, null);
    }


    @Override
    public String getAttributeDescription(final String type, final String attribute, final Locale locale)
    {
        final TypeFacadeStrategy strategy = strategyRegistry.getStrategy(type);
        if(strategy == null)
        {
            return null;
        }
        return strategy.getAttributeDescription(type, attribute, locale);
    }


    @Override
    public DataAttribute getAttribute(final Object object, final String attributeQualifier)
    {
        Validate.notNull("Object cannot be null", object);
        Validate.notNull("Attribute qualifier cannot be null", attributeQualifier);
        DataAttribute result = null;
        final TypeFacadeStrategy strategy = strategyRegistry.getStrategy(object.getClass().getName());
        if(strategy != null)
        {
            result = strategy.getAttribute(object, attributeQualifier);
        }
        return result;
    }
}
