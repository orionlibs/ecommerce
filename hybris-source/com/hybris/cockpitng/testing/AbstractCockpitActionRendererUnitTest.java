/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing;

import com.hybris.cockpitng.actions.CockpitActionRenderer;
import com.hybris.cockpitng.core.util.impl.ReflectionUtils;
import com.hybris.cockpitng.testing.util.internal.TestUtilInternal;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public abstract class AbstractCockpitActionRendererUnitTest<I, O, T extends CockpitActionRenderer<I, O>>
                extends AbstractCockpitngUnitTest<T>
{
    public abstract T getActionInstance();


    @Before
    public void setUpBaseActionUnitTest()
    {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testDefaultOrNoParameterConstructor()
    {
        Assertions.assertThat(TestUtilInternal.hasDefaultOrNoParameterConstructor(getWidgetType())).isTrue();
    }


    @Test
    public void testNullSafe()
    {
        final Set<Method> toCall = new HashSet<>();
        for(final Class<?> aClass : getWidgetType().getInterfaces())
        {
            toCall.addAll(TestUtilInternal.getAllMethods(aClass, true));
        }
        for(final Method method : getWidgetType().getDeclaredMethods())
        {
            final int modifiers = method.getModifiers();
            if(!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
            {
                toCall.add(method);
            }
        }
        testNullSafeMethods(getActionInstance(), toCall);
    }


    @Override
    protected Class<? extends T> getWidgetType()
    {
        return ReflectionUtils.extractGenericParameterType(getClass(), 2);
    }
}
