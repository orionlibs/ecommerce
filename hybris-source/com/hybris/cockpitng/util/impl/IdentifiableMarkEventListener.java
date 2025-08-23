/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.impl;

import com.hybris.cockpitng.core.model.Identifiable;
import com.hybris.cockpitng.util.MarkedEventListener;

/**
 * Marked component event listener with constant identity.
 * <P>
 * Marked components events listeners may be identifiable to assure that a particular listener is attached only once.
 * </P>
 */
public abstract class IdentifiableMarkEventListener implements MarkedEventListener, Identifiable
{
    private final Object id;


    public IdentifiableMarkEventListener(final Object id)
    {
        this.id = id;
    }


    @Override
    public Object getId()
    {
        return id;
    }
}
