/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model.impl;

import com.hybris.cockpitng.core.model.ModelObserver;
import com.hybris.cockpitng.core.util.Validate;

public abstract class AbstractIdentifiableModelObserver implements ModelObserver
{
    private final String id;


    public AbstractIdentifiableModelObserver(final String id)
    {
        Validate.notBlank("Id cannot be empty!", id);
        this.id = id;
    }


    @Override
    public String getId()
    {
        return id;
    }
}
