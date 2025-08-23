/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.dto;

import java.util.Objects;

/**
 * Represents the location of elements modelled in Workflow Designer
 */
public class ElementLocation
{
    public static final int ZERO_POSITION_X = 0;
    public static final int ZERO_POSITION_Y = 0;
    private final int x;
    private final int y;


    private ElementLocation(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }


    public int getX()
    {
        return x;
    }


    public int getY()
    {
        return y;
    }


    public static ElementLocation of(final int x, final int y)
    {
        return new ElementLocation(x, y);
    }


    public static ElementLocation zeroLocation()
    {
        return ElementLocation.of(ZERO_POSITION_X, ZERO_POSITION_Y);
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final ElementLocation that = (ElementLocation)o;
        return x == that.x && y == that.y;
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }
}
