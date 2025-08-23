/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model.impl;

import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;

/**
 * Bean resolver which provide access to beans configured in Spring Application Context AND specified in
 * availableBeanNames property.
 */
public class FixedBeanResolver implements BeanResolver, ApplicationContextAware
{
    private ApplicationContext applicationContext;


    @Override
    public Object resolve(final EvaluationContext context, final String beanName) throws AccessException
    {
        if(availableBeanNames != null && availableBeanNames.contains(beanName))
        {
            return applicationContext.getBean(beanName);
        }
        else
        {
            return null;
        }
    }


    private List<String> availableBeanNames;


    /**
     * @param availableBeanNames restrict available bean names
     */
    public void setAvailableBeanNames(final List<String> availableBeanNames)
    {
        this.availableBeanNames = availableBeanNames;
    }


    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }
}
