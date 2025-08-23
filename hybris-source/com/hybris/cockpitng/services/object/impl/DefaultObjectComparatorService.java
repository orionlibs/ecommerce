/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.object.impl;

import com.hybris.cockpitng.services.object.ObjectComparator;
import com.hybris.cockpitng.services.object.ObjectComparatorService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 * Allows to define comparators for particular objects that have not implemented correct equals.
 */
public class DefaultObjectComparatorService implements ObjectComparatorService
{
    private List<ObjectComparator> comparators;


    @Override
    public boolean equals(final Object first, final Object second)
    {
        for(final ObjectComparator comparator : this.comparators)
        {
            if(comparator.canCompare(first, second))
            {
                return comparator.equals(first, second);
            }
        }
        throw new IllegalArgumentException("Could not find suitable comparator for: [" + first + "] and [" + second + "]");
    }


    @Required
    public void setComparators(final List<ObjectComparator> comparators)
    {
        this.comparators = comparators;
    }
}
