/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.async.impl;

import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.async.Progress.ProgressType;

public abstract class AbstractOperation implements Operation
{
    private final String label;
    private boolean terminable;
    private ProgressType progressType = ProgressType.MANAGED;


    public AbstractOperation(final String label)
    {
        super();
        this.label = label;
    }


    public AbstractOperation(final String label, final boolean terminable)
    {
        this(label);
        this.terminable = terminable;
    }


    public AbstractOperation(final String label, final boolean terminable, final ProgressType progressType)
    {
        this(label, terminable);
        this.progressType = progressType;
    }


    @Override
    public String getLabel()
    {
        return label;
    }


    @Override
    public boolean isTerminable()
    {
        return terminable;
    }


    @Override
    public ProgressType getProgressType()
    {
        return progressType;
    }
}
