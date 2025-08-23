/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.adapters.flow;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import java.math.BigInteger;

/**
 * Wrapper class, which allows to interpert objects as Positioned
 * @param <T>
 */
public class PositionAware<T> extends Positioned
{
    private final T object;


    public T getObject()
    {
        return object;
    }


    public PositionAware(final T object, final BigInteger position)
    {
        this.object = object;
        PositionAware.this.setPosition(position);
    }
}
