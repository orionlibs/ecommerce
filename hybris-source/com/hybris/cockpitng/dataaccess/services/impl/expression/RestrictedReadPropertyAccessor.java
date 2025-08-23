/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.services.impl.expression;

import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

/**
 * {@link PropertyAccessor} wrapper, that checks current user permissions before it reads a value.
 * If user is not allowed to read a value, then {@link AccessException} is thrown.
 */
public class RestrictedReadPropertyAccessor implements PropertyAccessor
{
    private final PropertyAccessor accessor;
    private final PermissionFacade permissionFacade;


    public RestrictedReadPropertyAccessor(final PropertyAccessor accessor, final PermissionFacade permissionFacade)
    {
        this.accessor = accessor;
        this.permissionFacade = permissionFacade;
    }


    @Override
    public Class<?>[] getSpecificTargetClasses()
    {
        return accessor.getSpecificTargetClasses();
    }


    @Override
    public boolean canRead(final EvaluationContext evaluationContext, final Object object, final String expression)
                    throws AccessException
    {
        final boolean result = accessor.canRead(evaluationContext, object, expression);
        if(result && !permissionFacade.canReadInstanceProperty(object, expression))
        {
            throw new RestrictedAccessException(String.format("Insufficient read permissions for property '%s' of object %s",
                            expression, String.valueOf(object)));
        }
        return result;
    }


    @Override
    public TypedValue read(final EvaluationContext evaluationContext, final Object object, final String expression)
                    throws AccessException
    {
        return accessor.read(evaluationContext, object, expression);
    }


    @Override
    public boolean canWrite(final EvaluationContext evaluationContext, final Object object, final String expression)
                    throws AccessException
    {
        final boolean result = accessor.canWrite(evaluationContext, object, expression);
        if(result && !permissionFacade.canChangeInstanceProperty(object, expression))
        {
            throw new RestrictedAccessException(String.format("Insufficient write permissions for property '%s' of object %s",
                            expression, String.valueOf(object)));
        }
        return result;
    }


    @Override
    public void write(final EvaluationContext evaluationContext, final Object object, final String expression, final Object value)
                    throws AccessException
    {
        accessor.write(evaluationContext, object, expression, value);
    }
}
