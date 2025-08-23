/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.media.impl;

import com.hybris.cockpitng.core.util.impl.ReflectionUtils;
import com.hybris.cockpitng.services.media.PreviewResolutionStrategy;

public abstract class AbstractPreviewResolutionStrategy<T> implements PreviewResolutionStrategy<T>
{
    private final Class<T> clazz = ReflectionUtils.extractGenericParameterType(getClass(), 0);


    @Override
    public boolean canResolve(final Object target)
    {
        return target != null && clazz.isAssignableFrom(target.getClass());
    }
}
