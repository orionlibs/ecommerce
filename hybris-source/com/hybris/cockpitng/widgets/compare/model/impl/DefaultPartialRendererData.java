/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.model.impl;

import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.widgets.compare.model.ComparisonState;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;

public class DefaultPartialRendererData<D> implements PartialRendererData<D>
{
    private final ComparisonResult comparisonResult;
    private final D data;
    private final ComparisonState comparisonState;
    private boolean diffOnly;


    public DefaultPartialRendererData(final ComparisonResult comparisonResult, final D data, final ComparisonState comparisonState)
    {
        this.comparisonResult = comparisonResult;
        this.data = data;
        this.comparisonState = comparisonState;
    }


    @Override
    public D getData()
    {
        return data;
    }


    @Override
    public ComparisonResult getComparisonResult()
    {
        return comparisonResult;
    }


    @Override
    public ComparisonState getComparisonState()
    {
        return comparisonState;
    }


    @Override
    public boolean isDiffOnly()
    {
        return diffOnly;
    }


    public void setDiffOnly(final boolean showDiffOnly)
    {
        this.diffOnly = showDiffOnly;
    }
}
