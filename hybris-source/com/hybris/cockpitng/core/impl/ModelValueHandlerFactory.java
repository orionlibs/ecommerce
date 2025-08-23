/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.core.model.ModelValueHandler;
import com.hybris.cockpitng.core.model.impl.DefaultModelValueHandler;

public class ModelValueHandlerFactory
{
    private ExpressionResolverFactory expressionResolverFactory;


    public ModelValueHandler createModelValueHandler()
    {
        final DefaultModelValueHandler defaultModelValueHandler = new DefaultModelValueHandler();
        defaultModelValueHandler.setExpressionResolverFactory(getExpressionResolverFactory());
        return defaultModelValueHandler;
    }


    public ExpressionResolverFactory getExpressionResolverFactory()
    {
        return expressionResolverFactory;
    }


    public void setExpressionResolverFactory(final ExpressionResolverFactory expressionResolverFactory)
    {
        this.expressionResolverFactory = expressionResolverFactory;
    }
}
