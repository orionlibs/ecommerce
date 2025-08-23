/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util;

import org.springframework.context.expression.MapAccessor;
import org.springframework.core.Ordered;

public class OrderedMapAccessor extends MapAccessor implements Ordered
{
    private int order;


    @Override
    public int getOrder()
    {
        return order;
    }


    public void setOrder(final int order)
    {
        this.order = order;
    }
}
