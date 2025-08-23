/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.layout;

public class Point
{
    public static final Point NULL_POINT = new Point(0, 0);
    private int x;
    private int y;


    public Point(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }


    public int getX()
    {
        return x;
    }


    public void setX(final int x)
    {
        this.x = x;
    }


    public int getY()
    {
        return y;
    }


    public void setY(final int y)
    {
        this.y = y;
    }


    @Override
    public String toString()
    {
        if(this == NULL_POINT)
        {
            return "NULL_OBJECT";
        }
        return "(" + x + ", " + y + ")";
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        final Point point = (Point)o;
        return x == point.x && y == point.y;
    }


    @Override
    public int hashCode()
    {
        return 31 * x + y;
    }
}
