/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.expression.impl;

import com.hybris.cockpitng.core.expression.EvaluationContextFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Default implementation for the {@link com.hybris.cockpitng.core.expression.EvaluationContextFactory}.
 */
public class DefaultEvaluationContextFactory implements EvaluationContextFactory
{
    public static final String FUNCTION_NAME_EMPTY = "empty";
    public static final String FUNCTION_IS_BLANK = "isBlank";
    private List<PropertyAccessor> additionalPropertyAccessors;
    private List<MethodResolver> methodResolvers;
    private BeanResolver beanResolver;


    @Override
    public EvaluationContext createContext()
    {
        return createContext(Collections.emptyMap());
    }


    @Override
    public EvaluationContext createContext(final Map<String, Object> contextParameters)
    {
        EvaluationContext result = createEvaluationContext(null);
        result = initContext(result, contextParameters);
        return result;
    }


    @Override
    public EvaluationContext createContext(final Object rootObject, final Map<String, Object> contextParameters)
    {
        EvaluationContext result = createEvaluationContext(rootObject);
        result = initContext(result, contextParameters);
        return result;
    }


    protected EvaluationContext initContext(final EvaluationContext context, final Map<String, Object> contextParameters)
    {
        if(getAdditionalPropertyAccessors() != null && (context instanceof StandardEvaluationContext))
        {
            getAdditionalPropertyAccessors().forEach(((StandardEvaluationContext)context)::addPropertyAccessor);
        }
        if(getMethodResolvers() != null)
        {
            ((StandardEvaluationContext)context).setMethodResolvers(methodResolvers);
        }
        if(getBeanResolver() != null)
        {
            ((StandardEvaluationContext)context).setBeanResolver(beanResolver);
        }
        try
        {
            ((StandardEvaluationContext)context).registerFunction(FUNCTION_NAME_EMPTY,
                            StringUtils.class.getMethod(FUNCTION_IS_BLANK, CharSequence.class));
        }
        catch(final NoSuchMethodException noSuchMethod)
        {
            throw new IllegalStateException(noSuchMethod);
        }
        for(final Map.Entry<String, Object> entry : contextParameters.entrySet())
        {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return context;
    }


    protected EvaluationContext createEvaluationContext(final Object rootObject)
    {
        final StandardEvaluationContext ctx = new StandardEvaluationContext();
        if(rootObject != null)
        {
            ctx.setRootObject(rootObject);
        }
        return ctx;
    }


    public List<PropertyAccessor> getAdditionalPropertyAccessors()
    {
        return additionalPropertyAccessors;
    }


    public List<MethodResolver> getMethodResolvers()
    {
        return methodResolvers;
    }


    public BeanResolver getBeanResolver()
    {
        return beanResolver;
    }


    public void setAdditionalPropertyAccessors(final List<PropertyAccessor> additionalPropertyAccessors)
    {
        this.additionalPropertyAccessors = additionalPropertyAccessors;
    }


    public void setMethodResolvers(final List<MethodResolver> methodResolvers)
    {
        this.methodResolvers = methodResolvers;
    }


    public void setBeanResolver(final BeanResolver beanResolver)
    {
        this.beanResolver = beanResolver;
    }
}
