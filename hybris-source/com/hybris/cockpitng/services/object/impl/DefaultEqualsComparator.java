/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.services.object.impl;

import com.hybris.cockpitng.services.object.ObjectComparator;

public class DefaultEqualsComparator implements ObjectComparator
{
    @Override
    public boolean canCompare(final Object first, final Object second)
    {
        return true;
    }


    @Override
    public boolean equals(final Object first, final Object second)
    {
        if(first == null)
        {
            return false;
        }
        return first.equals(second);
    }
}
