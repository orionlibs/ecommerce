/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.model;

import com.hybris.cockpitng.compare.model.ComparisonResult;
import java.util.Collection;

public class CompareViewData<I>
{
    private final ComparisonResult compareResults;
    private final ComparisonState<I> comparisonState;
    private boolean diffOnly;


    /**
     * @deprecated since 1808, use @link{@link #CompareViewData(ComparisonResult, ComparisonState, boolean)}
     */
    @Deprecated(since = "1808", forRemoval = true)
    public CompareViewData(final ComparisonResult compareResults, final ComparisonState<I> comparisonState)
    {
        this.compareResults = compareResults;
        this.comparisonState = comparisonState;
    }


    public CompareViewData(final ComparisonResult compareResults, final ComparisonState<I> comparisonState, final boolean diffOnly)
    {
        this(compareResults, comparisonState);
        this.diffOnly = diffOnly;
    }


    public I getReference()
    {
        return getComparisonState().getReference();
    }


    public Collection<I> getTargets()
    {
        return getComparisonState().getAllObjects();
    }


    public ComparisonResult getComparisonResult()
    {
        return compareResults;
    }


    public ComparisonState<I> getComparisonState()
    {
        return comparisonState;
    }


    public boolean isDiffOnly()
    {
        return diffOnly;
    }


    public boolean merge(final ComparisonState status, final ComparisonResult addition, final Object comparedObjectId)
    {
        if(status == comparisonState)
        {
            return compareResults.merge(addition, comparedObjectId);
        }
        return false;
    }
}
