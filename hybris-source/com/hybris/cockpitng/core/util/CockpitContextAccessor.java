/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util;

import com.hybris.cockpitng.core.context.CockpitContext;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class CockpitContextAccessor implements PropertyAccessor
{
    @Override
    public Class<?>[] getSpecificTargetClasses()
    {
        return new Class<?>[]
                        {CockpitContext.class};
    }


    @Override
    public boolean canRead(final EvaluationContext context, final Object target, final String name)
    {
        return target == null || (target instanceof CockpitContext && ((CockpitContext)target).containsParameter(name));
    }


    protected Map<String, Object> getParameters(final Object target)
    {
        return ObjectUtils.defaultIfNull(((CockpitContext)target).getParameters(), Collections.<String, Object>emptyMap());
    }


    @Override
    public TypedValue read(final EvaluationContext context, final Object target, final String name)
    {
        return new TypedValue(getParameters(target).get(name));
    }


    @Override
    public boolean canWrite(final EvaluationContext context, final Object target, final String name)
    {
        return name != null && target instanceof CockpitContext;
    }


    @Override
    public void write(final EvaluationContext context, final Object target, final String name, final Object newValue)
    {
        ((CockpitContext)target).setParameter(name, newValue);
    }
}
