/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import java.util.function.Supplier;

/**
 * A label service cache that does not cache values and directly calls default value supplier
 */
public class NoCacheLabelServiceCache implements LabelServiceCache
{
    @Override
    public String getObjectLabel(final Object object, final Supplier<String> defaultValue)
    {
        return defaultValue.get();
    }


    @Override
    public String getShortObjectLabel(final Object object, final Supplier<String> defaultValue)
    {
        return defaultValue.get();
    }


    @Override
    public String getObjectDescription(final Object object, final Supplier<String> defaultValue)
    {
        return defaultValue.get();
    }


    @Override
    public String getObjectIconPath(final Object object, final Supplier<String> defaultValue)
    {
        return defaultValue.get();
    }
}
