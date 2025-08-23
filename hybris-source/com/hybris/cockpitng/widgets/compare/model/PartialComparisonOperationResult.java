/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.model;

import com.hybris.cockpitng.compare.model.ComparisonResult;

public class PartialComparisonOperationResult
{
    private final ComparisonResult comparisonResult;
    private final Object referenceObjectId;
    private final Object comparedObjectId;
    private final boolean referenceObjectValid;
    private final boolean comparedObjectValid;


    public PartialComparisonOperationResult(final ComparisonResult comparisonResult, final Object referenceObjectId,
                    final Object comparedObjectId, final boolean referenceObjectValid, final boolean comparedObjectValid)
    {
        this.comparisonResult = comparisonResult;
        this.referenceObjectId = referenceObjectId;
        this.comparedObjectId = comparedObjectId;
        this.referenceObjectValid = referenceObjectValid;
        this.comparedObjectValid = comparedObjectValid;
    }


    public ComparisonResult getComparisonResult()
    {
        return comparisonResult;
    }


    public Object getReferenceObjectId()
    {
        return referenceObjectId;
    }


    public Object getComparedObjectId()
    {
        return comparedObjectId;
    }


    public boolean isReferenceObjectValid()
    {
        return referenceObjectValid;
    }


    public boolean isComparedObjectValid()
    {
        return comparedObjectValid;
    }
}