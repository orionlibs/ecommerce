/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.expression.impl;

import com.hybris.cockpitng.core.expression.EvaluationContextFactory;
import com.hybris.cockpitng.core.expression.ExpressionResolver;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.expression.ExpressionParser;

public class DefaultExpressionResolverFactory implements ExpressionResolverFactory
{
    private EvaluationContextFactory contextFactory;
    private ExpressionParser expressionParser;


    @Override
    public ExpressionResolver createResolver()
    {
        final DefaultExpressionResolver resolver = new DefaultExpressionResolver(getContextFactory());
        if(getExpressionParser() != null)
        {
            resolver.setExpressionParser(getExpressionParser());
        }
        return resolver;
    }


    public EvaluationContextFactory getContextFactory()
    {
        return contextFactory;
    }


    @Required
    public void setContextFactory(final EvaluationContextFactory contextFactory)
    {
        this.contextFactory = contextFactory;
    }


    public ExpressionParser getExpressionParser()
    {
        return expressionParser;
    }


    public void setExpressionParser(final ExpressionParser expressionParser)
    {
        this.expressionParser = expressionParser;
    }
}
