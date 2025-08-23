/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.model;

import com.hybris.cockpitng.core.context.CockpitContext;

/**
 * Interface for generation of models required by UI components.
 *
 * @param <T> Type of the returned model
 */
public interface ComponentModelPopulator<T>
{
    /**
     * Method used to create the model using the provided context.
     *
     * @param context creation context
     * @return requested model
     */
    T createModel(CockpitContext context);
}
