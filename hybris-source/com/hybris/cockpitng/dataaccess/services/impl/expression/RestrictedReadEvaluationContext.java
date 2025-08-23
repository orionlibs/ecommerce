/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.services.impl.expression;

import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ConstructorResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.OperatorOverloader;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypeComparator;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.TypeLocator;
import org.springframework.expression.TypedValue;

/**
 * {@link EvaluationContext} wrapper, that checks current user permissions during expression evaluation. Each
 * {@link PropertyAccessor} is wrapped with {@link RestrictedReadPropertyAccessor}.
 */
public class RestrictedReadEvaluationContext implements EvaluationContext
{
    private final EvaluationContext context;
    private final PermissionFacade permissionFacade;
    private List<PropertyAccessor> accessors;


    public RestrictedReadEvaluationContext(final EvaluationContext context, final PermissionFacade permissionFacade)
    {
        this.context = context;
        this.permissionFacade = permissionFacade;
    }


    @Override
    public TypedValue getRootObject()
    {
        return context.getRootObject();
    }


    @Override
    public List<ConstructorResolver> getConstructorResolvers()
    {
        return context.getConstructorResolvers();
    }


    @Override
    public List<MethodResolver> getMethodResolvers()
    {
        return context.getMethodResolvers();
    }


    protected PropertyAccessor wrapAccesor(final PropertyAccessor accessor)
    {
        return new RestrictedReadPropertyAccessor(accessor, permissionFacade);
    }


    @Override
    public List<PropertyAccessor> getPropertyAccessors()
    {
        if(accessors == null)
        {
            accessors = context.getPropertyAccessors().stream().map(this::wrapAccesor).collect(Collectors.toList());
        }
        return accessors;
    }


    @Override
    public TypeLocator getTypeLocator()
    {
        return context.getTypeLocator();
    }


    @Override
    public TypeConverter getTypeConverter()
    {
        return context.getTypeConverter();
    }


    @Override
    public TypeComparator getTypeComparator()
    {
        return context.getTypeComparator();
    }


    @Override
    public OperatorOverloader getOperatorOverloader()
    {
        return context.getOperatorOverloader();
    }


    @Override
    public BeanResolver getBeanResolver()
    {
        return context.getBeanResolver();
    }


    @Override
    public void setVariable(final String key, final Object value)
    {
        context.setVariable(key, value);
    }


    @Override
    public Object lookupVariable(final String variable)
    {
        return context.lookupVariable(variable);
    }
}