/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model.impl;

import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.core.model.ModelValueHandler;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ExpressionException;

public class DefaultModelValueHandler implements ModelValueHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultModelValueHandler.class);
    private ExpressionResolverFactory expressionResolverFactory;


    @Override
    public final void setValue(final Object model, final String key, final Object value)
    {
        if(model != null && StringUtils.isNotEmpty(key))
        {
            setSimpleValue(model, key, value);
        }
        else
        {
            final String message = String.format("Could not set value '%s' in model class '%s'.", key, model);
            if(LOG.isDebugEnabled())
            {
                LOG.error(message, new IllegalArgumentException());
            }
            else
            {
                LOG.error(message);
            }
        }
    }


    private void setSimpleValue(final Object model, final String key, final Object value)
    {
        try
        {
            setValueThroughResolver(model, key, value);
        }
        catch(final EvaluationException e)
        {
            if(model instanceof Map)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("No method found for property '%s', using attribute fallback.", key), e);
                }
                ((Map)model).put(key, value);
            }
            else
            {
                LOG.error(String.format("No method found for property '%s'.", key), e);
            }
        }
    }


    protected void setValueThroughResolver(final Object model, final String key, final Object value)
    {
        getExpressionResolverFactory().createResolver().setValue(model, key, value);
    }


    @Override
    public Object getValue(final Object model, final String key)
    {
        return getValue(model, key, false);
    }


    @Override
    public Object getValue(final Object model, final String key, final boolean useSessionLanguageForLocalized)
    {
        if(model != null && StringUtils.isNotEmpty(key))
        {
            return getSimpleValue(model, key, useSessionLanguageForLocalized);
        }
        final String message = String.format("Could not get value '%s' from model class '%s', returning null.", key, model);
        if(LOG.isDebugEnabled())
        {
            LOG.error(message, new IllegalArgumentException());
        }
        else
        {
            LOG.error(message);
        }
        return null;
    }


    private Object getSimpleValue(final Object model, final String key, final boolean useSessionLanguageForLocalized)
    {
        try
        {
            final Map<String, Object> variables = new HashMap<>();
            variables.put("useSessionLanguageForLocalized", useSessionLanguageForLocalized);
            return getValueFromResolver(model, key, variables);
        }
        catch(final EvaluationException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("No method found for property '%s', using attribute fallback", key), e);
            }
            if(model instanceof Map)
            {
                return ((Map)model).get(key);
            }
            return null;
        }
    }


    protected Object getValueFromResolver(final Object model, final String key, final Map<String, Object> variables)
    {
        return getExpressionResolverFactory().createResolver().getValue(model, key, variables);
    }


    @Override
    public <T> Class<T> getValueType(final Object model, final String key)
    {
        if(StringUtils.isNotBlank(key))
        {
            if(isSimpleKey(key))
            {
                return getSimpleValueType(model, key);
            }
            else
            {
                final Object rootObject = resolveRootObject(model, key);
                final String expression = getExpression(key);
                try
                {
                    return getExpressionResolverFactory().createResolver().getValueType(rootObject, expression);
                }
                catch(final ExpressionException e)
                {
                    LOG.debug(String.format("No method found for property '%s', using fallback", key), e);
                }
            }
            LOG.debug("Could not handle model class, returning Object value type.");
        }
        return (Class<T>)Object.class;
    }


    private <T> Class<T> getSimpleValueType(final Object model, final String key)
    {
        try
        {
            return getExpressionResolverFactory().createResolver().getValueType(model, key);
        }
        catch(final ExpressionException e)
        {
            LOG.debug(String.format("No method found for property '%s'", key), e);
        }
        return (Class<T>)Object.class;
    }


    private boolean isSimpleKey(final String key)
    {
        return !key.contains(".");
    }


    private String getExpression(final String key)
    {
        final int index = key.indexOf('.');
        return index > 0 ? key.substring(index + 1) : null;
    }


    private Object resolveRootObject(final Object model, final String key)
    {
        final int index = key.indexOf('.');
        if(index > 0)
        {
            final String varString = key.substring(0, index);
            return getSimpleValue(model, varString, false);
        }
        else
        {
            LOG.error("No '.' in key");
        }
        return null;
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
