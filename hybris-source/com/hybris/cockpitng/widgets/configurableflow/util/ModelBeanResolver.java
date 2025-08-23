/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.util;

import com.hybris.cockpitng.core.model.WidgetModel;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;

/**
 * Model bean resolver.
 */
public class ModelBeanResolver implements BeanResolver
{
    private final WidgetModel model;


    public ModelBeanResolver(final WidgetModel model)
    {
        this.model = model;
    }


    @Override
    public Object resolve(final EvaluationContext context, final String beanName) throws AccessException
    {
        return model.getValue(beanName, Object.class);
    }
}
