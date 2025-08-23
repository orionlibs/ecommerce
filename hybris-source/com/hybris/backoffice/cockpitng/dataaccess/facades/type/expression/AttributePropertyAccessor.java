/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.type.expression;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

/**
 *
 *
 */
public class AttributePropertyAccessor implements PropertyAccessor
{
    private final AttributeEvaluationContext context;
    private final PropertyAccessor accessor;


    public AttributePropertyAccessor(final AttributeEvaluationContext context, final PropertyAccessor accessor)
    {
        this.accessor = accessor;
        this.context = context;
    }


    @Override
    public Class<?>[] getSpecificTargetClasses()
    {
        return accessor.getSpecificTargetClasses();
    }


    @Override
    public boolean canRead(final EvaluationContext evaluationContext, final Object object, final String expression) throws AccessException
    {
        return accessor.canRead(evaluationContext, object, expression);
    }


    @Override
    public TypedValue read(final EvaluationContext evaluationContext, final Object object, final String expression) throws AccessException
    {
        final TypedValue value = accessor.read(evaluationContext, object, expression);
        context.registerEvaluation(object, expression, value);
        return value;
    }


    @Override
    public boolean canWrite(final EvaluationContext evaluationContext, final Object object, final String expression) throws AccessException
    {
        return accessor.canWrite(evaluationContext, object, expression);
    }


    @Override
    public void write(final EvaluationContext evaluationContext, final Object object, final String expression, final Object value) throws AccessException
    {
        accessor.write(evaluationContext, object, expression, value);
    }
}