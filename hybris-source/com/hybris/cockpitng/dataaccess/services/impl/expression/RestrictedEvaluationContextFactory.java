/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.services.impl.expression;

import com.hybris.cockpitng.core.expression.EvaluationContextFactory;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.expression.EvaluationContext;

/**
 * Decorator of factory for {@link org.springframework.expression.EvaluationContext} that checks whether user has enough
 * privileges to evaluate expression.
 */
public class RestrictedEvaluationContextFactory implements EvaluationContextFactory
{
    private EvaluationContextFactory factory;
    private PermissionFacade permissionFacade;


    protected PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    @Required
    public void setFactory(final EvaluationContextFactory factory)
    {
        this.factory = factory;
    }


    @Override
    public EvaluationContext createContext()
    {
        final EvaluationContext context = factory.createContext();
        return new RestrictedReadEvaluationContext(context, getPermissionFacade());
    }


    @Override
    public EvaluationContext createContext(final Map<String, Object> contextParameters)
    {
        final EvaluationContext context = factory.createContext(contextParameters);
        return new RestrictedReadEvaluationContext(context, getPermissionFacade());
    }


    @Override
    public EvaluationContext createContext(final Object rootObject, final Map<String, Object> contextParameters)
    {
        final EvaluationContext context = factory.createContext(rootObject, contextParameters);
        return new RestrictedReadEvaluationContext(context, getPermissionFacade());
    }
}
