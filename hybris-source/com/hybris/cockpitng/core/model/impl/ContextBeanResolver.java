/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;

public class ContextBeanResolver implements ApplicationContextAware, BeanResolver
{
    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }


    @Override
    public Object resolve(final EvaluationContext evaluationContext, final String beanName) throws AccessException
    {
        try
        {
            return applicationContext.getBean(beanName);
        }
        catch(final BeansException ex)
        {
            throw new AccessException(ex.getLocalizedMessage(), ex);
        }
    }
}
