/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.annotation;

import com.hybris.cockpitng.testing.providers.NullProxyMethodValueProvider;
import com.hybris.cockpitng.testing.providers.ProxyMethodValueProvider;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface NullSafeWidget
{
    int ALL = 1 << 0;
    int DEFAULT_VALUES = 1 << 1;
    int PROXY_VALUES = 1 << 2;
    int PROXY_EXCEPTIONS = 1 << 3;


    int level() default ALL;


    boolean value() default true;


    Class<? extends ProxyMethodValueProvider> proxyMethodValueProvider() default NullProxyMethodValueProvider.class;
}
