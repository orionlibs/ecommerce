/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model;

/**
 * Listener for changes of model properties going through the widget controller.
 */
public interface ModelObserver extends Identifiable, ValueObserver
{
    @Override
    default Object getId()
    {
        return null;
    }
}
