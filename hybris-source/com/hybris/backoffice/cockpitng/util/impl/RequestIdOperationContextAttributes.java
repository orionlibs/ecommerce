/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.util.impl;

import com.hybris.backoffice.cockpitng.util.BackofficeThreadRequestAttributes;
import com.hybris.cockpitng.core.spring.RequestOperationContextManager;
import java.util.Collections;
import java.util.Enumeration;
import javax.servlet.ServletRequest;

/**
 * A request operation context manager that assures that request id is copied to
 * {@link com.hybris.backoffice.cockpitng.util.BackofficeThreadContextCreator} executors.
 */
public class RequestIdOperationContextAttributes extends RequestOperationContextManager
                implements BackofficeThreadRequestAttributes
{
    @Override
    public Enumeration<String> getAttributeNames(final ServletRequest request)
    {
        return Collections.list(request.getAttributeNames()).contains(ATTRIBUTE_REQUEST_ID)
                        ? Collections.enumeration(Collections.singleton(ATTRIBUTE_REQUEST_ID))
                        : Collections.emptyEnumeration();
    }


    @Override
    public Object getAttribute(final ServletRequest request, final String attribute)
    {
        return request.getAttribute(attribute);
    }
}
