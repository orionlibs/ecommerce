/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.expression.impl;

import com.hybris.cockpitng.core.expression.EvaluationContextFactory;
import com.hybris.cockpitng.core.expression.ExpressionResolver;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Default implementation of {@link com.hybris.cockpitng.core.expression.ExpressionResolver}
 */
public class DefaultExpressionResolver implements ExpressionResolver
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultExpressionResolver.class);
    private final EvaluationContextFactory contextFactory;
    private ExpressionParser expressionParser;


    public DefaultExpressionResolver(final EvaluationContextFactory contextFactory)
    {
        this.contextFactory = contextFactory;
    }


    protected EvaluationContextFactory getContextFactory()
    {
        return contextFactory;
    }


    protected ExpressionParser getExpressionParser()
    {
        if(this.expressionParser == null)
        {
            this.expressionParser = createExpressionParser();
        }
        return this.expressionParser;
    }


    public void setExpressionParser(final ExpressionParser expressionParser)
    {
        this.expressionParser = expressionParser;
    }


    protected ExpressionParser createExpressionParser()
    {
        return new SpelExpressionParser();
    }


    @Override
    public <T> T getValue(final Object sourceObject, final String expression)
    {
        return getValue(sourceObject, expression, Collections.emptyMap());
    }


    @Override
    public <T> T getValue(final Object sourceObject, final String expression, final Map<String, Object> variables)
    {
        try
        {
            final EvaluationContext context = getContextFactory().createContext(sourceObject, variables);
            return (T)getExpressionParser().parseExpression(expression).getValue(context, sourceObject);
        }
        catch(final ExpressionException ex)
        {
            LOG.debug("Could not evaluate SpEL: {}\n\tOn object: {}\n\tWith context params: {}", expression, sourceObject,
                            variables);
            throw ex;
        }
    }


    @Override
    public void setValue(final Object sourceObject, final String expression, final Object value)
    {
        try
        {
            final EvaluationContext context = getContextFactory().createContext(sourceObject, Collections.emptyMap());
            getExpressionParser().parseExpression(expression).setValue(context, sourceObject, value);
        }
        catch(final ExpressionException ex)
        {
            LOG.debug("Could not evaluate SpEL: {}\n\tOn object: {}\n\tWith value: {}", expression, sourceObject, value);
            throw ex;
        }
    }


    @Override
    public void setValue(final Object sourceObject, final String expression, final Object value, final Locale locale)
    {
        setValue(sourceObject, expression, Collections.singletonMap(locale, value));
    }


    @Override
    public <T> Class<T> getValueType(final Object sourceObject, final String expression, final Map<String, Object> variables)
    {
        try
        {
            final EvaluationContext context = getContextFactory().createContext(sourceObject, variables);
            return (Class<T>)getExpressionParser().parseExpression(expression).getValueType(context, sourceObject);
        }
        catch(final ExpressionException ex)
        {
            LOG.debug("Could not evaluate SpEL: {}\n\tOn object: {}\n\tWith context params: {}", expression, sourceObject,
                            variables);
            throw ex;
        }
    }


    @Override
    public <T> Class<T> getValueType(final Object sourceObject, final String expression)
    {
        return getValueType(sourceObject, expression, Collections.emptyMap());
    }
}
