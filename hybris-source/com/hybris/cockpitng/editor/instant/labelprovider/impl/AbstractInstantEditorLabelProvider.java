/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant.labelprovider.impl;

import com.hybris.cockpitng.editor.instant.labelprovider.InstantEditorLabelProvider;

/**
 * Abstract implementation of {@link InstantEditorLabelProvider} interface. Takes care of order related functionality.
 * <p>
 * All specific implementations should extend this class instead of implementing interface themselves.
 * </p>
 */
public abstract class AbstractInstantEditorLabelProvider implements InstantEditorLabelProvider
{
    private int loadOrder;


    /**
     * Order in which implementing classes should be checked if they can handle given value. Lower values take higher
     * precedence.
     */
    @Override
    public int getOrder()
    {
        return loadOrder;
    }


    public void setOrder(final int order)
    {
        this.loadOrder = order;
    }
}
