/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.layout;

import com.hybris.cockpitng.core.util.Validate;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class is used to describe element's position.
 *
 * @param <T>
 *           Type of element
 */
public class ElementPlacement<T>
{
    private static final Logger LOG = LoggerFactory.getLogger(ElementPlacement.class);
    private Point topLeft;
    private Point bottomRight;
    private T element;


    public ElementPlacement(final Point p1, final Point p2)
    {
        this(null, p1, p2);
    }


    public ElementPlacement(final T element, final Point p1, final Point p2)
    {
        Validate.notNull("Coordinates may not be null", p1, p2);
        this.element = element;
        if(p1 == Point.NULL_POINT || p2 == Point.NULL_POINT)
        {
            if(p1 != Point.NULL_POINT || p2 != Point.NULL_POINT)
            {
                LOG.warn("Both coordinates must be both defined or undefined, setting both coordinates to undefined");
            }
            topLeft = Point.NULL_POINT;
            bottomRight = Point.NULL_POINT;
        }
        else
        {
            final int x1 = Math.min(p1.getX(), p2.getX());
            final int x2 = Math.max(p1.getX(), p2.getX());
            final int y1 = Math.min(p1.getY(), p2.getY());
            final int y2 = Math.max(p1.getY(), p2.getY());
            topLeft = new Point(x1, y1);
            bottomRight = new Point(x2, y2);
        }
    }


    /**
     * Tels if given coordinates are inside element placement's coordinates {@link #topLeft} and {@link #bottomRight}
     * inclusively.
     *
     * @param x
     *           x coordinate.
     * @param y
     *           y coordinate.
     * @return true if coordinates are inside element placement.
     */
    public boolean contains(final int x, final int y)
    {
        return topLeft.getX() <= x && topLeft.getY() <= y && bottomRight.getX() >= x && bottomRight.getY() >= y;
    }


    public Point getTopLeft()
    {
        return topLeft;
    }


    public void setTopLeft(final Point topLeft)
    {
        this.topLeft = topLeft;
    }


    public Point getBottomRight()
    {
        return bottomRight;
    }


    public void setBottomRight(final Point bottomRight)
    {
        this.bottomRight = bottomRight;
    }


    public T getElement()
    {
        return element;
    }


    public void setElement(final T element)
    {
        this.element = element;
    }


    public int getHeight()
    {
        return getBottomRight().getY() - getTopLeft().getY() + 1;
    }


    public int getWidth()
    {
        return getBottomRight().getX() - getTopLeft().getX() + 1;
    }


    @Override
    public String toString()
    {
        return ObjectUtils.toString(getElement()) + ": [" + getTopLeft().toString() + ", " + getBottomRight().toString() + "]";
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
        final ElementPlacement<?> that = (ElementPlacement<?>)o;
        if(topLeft != null ? !topLeft.equals(that.topLeft) : that.topLeft != null)
        {
            return false;
        }
        if(bottomRight != null ? !bottomRight.equals(that.bottomRight) : that.bottomRight != null)
        {
            return false;
        }
        return !(element != null ? !element.equals(that.element) : that.element != null);
    }


    @Override
    public int hashCode()
    {
        int result = topLeft != null ? topLeft.hashCode() : 0;
        result = 31 * result + (bottomRight != null ? bottomRight.hashCode() : 0);
        result = 31 * result + (element != null ? element.hashCode() : 0);
        return result;
    }
}
