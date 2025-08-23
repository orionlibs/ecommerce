/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model;

import com.hybris.cockpitng.core.impl.DefaultWidgetModel;
import com.hybris.cockpitng.core.util.OrderedMapAccessor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.TypedValue;

/**
 * Accessor for {@link DefaultWidgetModel} which returns null when there is no value for given key as opposed to
 * {@link OrderedMapAccessor} which throws an exception.
 */
public class DefaultWidgetModelAccessor extends OrderedMapAccessor
{
    @Override
    public Class<?>[] getSpecificTargetClasses()
    {
        return new Class<?>[] {DefaultWidgetModel.class};
    }


    @Override
    public boolean canRead(final EvaluationContext context, final Object target, final String name) throws AccessException
    {
        return true;
    }


    @Override
    public TypedValue read(final EvaluationContext context, final Object target, final String name) throws AccessException
    {
        final DefaultWidgetModel map = (DefaultWidgetModel)target;
        final Object value = map.get(name);
        if(value == null && !map.containsKey(name))
        {
            return TypedValue.NULL;
        }
        return new TypedValue(value);
    }
}
