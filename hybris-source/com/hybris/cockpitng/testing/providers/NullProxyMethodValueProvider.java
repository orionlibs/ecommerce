/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.providers;

import java.lang.reflect.Method;

public final class NullProxyMethodValueProvider implements ProxyMethodValueProvider
{
    private NullProxyMethodValueProvider()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    @Override
    public boolean canProvideValue(final Method method)
    {
        return false;
    }


    @Override
    public Object getValue(final Method method)
    {
        throw new UnsupportedOperationException();
    }
}
