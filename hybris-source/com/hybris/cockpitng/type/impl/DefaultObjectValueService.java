/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.type.impl;

import com.hybris.cockpitng.core.impl.ModelValueHandlerFactory;
import com.hybris.cockpitng.core.model.ModelValueHandler;
import com.hybris.cockpitng.type.ObjectValueService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.expression.EvaluationException;

/**
 * Service that handle values access by SpEL language.
 */
public class DefaultObjectValueService implements ObjectValueService
{
    private ModelValueHandler valueHandler;
    private ModelValueHandlerFactory modelValueHandlerFactory;


    @Override
    public <T> T getValue(final String expression, final Object object) throws EvaluationException
    {
        return (T)this.getValueHandler().getValue(object, expression);
    }


    @Required
    public void setModelValueHandlerFactory(final ModelValueHandlerFactory modelValueHandlerFactory)
    {
        this.modelValueHandlerFactory = modelValueHandlerFactory;
    }


    @Override
    public void setValue(final String expression, final Object object, final Object value) throws EvaluationException
    {
        this.getValueHandler().setValue(object, expression, value);
    }


    private ModelValueHandler getValueHandler()
    {
        if(this.valueHandler == null)
        {
            this.valueHandler = this.modelValueHandlerFactory.createModelValueHandler();
        }
        return this.valueHandler;
    }
}
