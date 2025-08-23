/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.spring;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * Servlet request listener that initializes request-operation scope context when request is initialized and detaches
 * from it when request is destroyed.
 */
public class RequestOperationContextManager implements ServletRequestListener
{
    /**
     * @deprecated since 2105, not used any more
     */
    @Deprecated(since = "2105", forRemoval = true)
    protected static final String ATTRIBUTE_REQUEST_ID = "request-id";


    /**
     * Gets an identity of a request processed by current thread. If thread is not bound to any request, <code>null</code>
     * is returned.
     *
     * @return identity of currently processed request or <code>null</code> if it doesn't exist
     * @deprecated since 2105, not used any more
     */
    @Deprecated(since = "2105", forRemoval = true)
    public static Object getRequestId()
    {
        return null;
    }


    @Override
    public void requestInitialized(final ServletRequestEvent servletRequestEvent)
    {
        RequestOperationContextHolder.instance().initializeContext();
    }


    @Override
    public void requestDestroyed(final ServletRequestEvent servletRequestEvent)
    {
        RequestOperationContextHolder.instance().detachFromContext();
    }
}
