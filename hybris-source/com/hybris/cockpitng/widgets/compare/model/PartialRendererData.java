/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.model;

import com.hybris.cockpitng.compare.model.ComparisonResult;

/**
 * A data for a partial CompareView renderer (i.e. an attribute value renderer). Depending on renderer a data may
 * contain single item to be rendered or a colletion.
 *
 * @param <D>
 *           type of data contained by this object
 */
public interface PartialRendererData<D>
{
    /**
     * @return data to be rendered
     */
    D getData();


    /**
     * @return current comparison result
     */
    ComparisonResult getComparisonResult();


    /**
     * @return current state of comparison
     */
    ComparisonState getComparisonState();


    /**
     * @return <code>true</code> if only differences should be rendered
     */
    default boolean isDiffOnly()
    {
        return false;
    }
}
