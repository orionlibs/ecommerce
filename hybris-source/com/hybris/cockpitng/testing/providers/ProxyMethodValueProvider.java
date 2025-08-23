/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.providers;

import java.lang.reflect.Method;

public interface ProxyMethodValueProvider
{
    boolean canProvideValue(final Method method);


    Object getValue(final Method method);
}
