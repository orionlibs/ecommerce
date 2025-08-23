/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.spring;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Spring scope for request and all operations it triggers.
 * <P>
 * Operations should attach to scope before execution using
 * {@link RequestOperationContextHolder#attachToContext(RequestOperationContextHolder.ContextRequest)} and detach after
 * finishing using {@link RequestOperationContextHolder#detachFromContext()}.
 * </P>
 *
 * @see RequestOperationContextHolder
 *
 * @deprecated since 2105, not used any more
 */
@Deprecated(since = "2105", forRemoval = true)
public class RequestOperationScope implements Scope
{
    private static boolean hasScopeContext()
    {
        return RequestOperationContextHolder.instance().hasContext();
    }


    private static RequestOperationContext getScopeContext()
    {
        return null;
    }


    @Override
    public String getConversationId()
    {
        return null;
    }


    @Override
    public Object get(final String name, final ObjectFactory<?> factory)
    {
        return factory.getObject();
    }


    @Override
    public void registerDestructionCallback(final String name, final Runnable callback)
    {
        // Do nothing.
    }


    @Override
    public Object remove(final String name)
    {
        return null;
    }


    @Override
    public Object resolveContextualObject(final String objectName)
    {
        return RequestContextHolder.currentRequestAttributes().resolveReference(objectName);
    }


    protected RequestOperationContext getContext()
    {
        return getScopeContext();
    }
}
