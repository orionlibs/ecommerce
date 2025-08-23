/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.type.expression;

import com.hybris.cockpitng.core.expression.EvaluationContextFactory;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.expression.EvaluationContext;

/**
 *
 * Factory for {@link AttributeEvaluationContext}
 */
public class AttributeEvaluationContextFactory implements EvaluationContextFactory
{
    private EvaluationContextFactory contextFactory;


    protected EvaluationContextFactory getContextFactory()
    {
        return contextFactory;
    }


    @Required
    public void setContextFactory(final EvaluationContextFactory contextFactory)
    {
        this.contextFactory = contextFactory;
    }


    @Override
    public EvaluationContext createContext()
    {
        return new AttributeEvaluationContext(getContextFactory().createContext());
    }


    @Override
    public EvaluationContext createContext(final Map<String, Object> variables)
    {
        return new AttributeEvaluationContext(getContextFactory().createContext(variables));
    }


    @Override
    public EvaluationContext createContext(final Object rootObject, final Map<String, Object> contextParameters)
    {
        return new AttributeEvaluationContext(getContextFactory().createContext(rootObject, contextParameters));
    }
}
