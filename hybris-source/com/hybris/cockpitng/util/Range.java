/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.components.Editor;
import java.io.Serializable;

/**
 * Range object used in {@link Editor} of type "Range"
 */
public class Range<T> implements Serializable
{
    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -1608210209287110879L;
    private T start;
    private T end;


    public Range()
    {
        // No default implementation
    }


    public Range(final T start, final T end)
    {
        this.start = start;
        this.end = end;
    }


    /**
     * @return the start
     */
    public T getStart()
    {
        return start;
    }


    /**
     * @param start the start to set
     */
    public void setStart(final T start)
    {
        this.start = start;
    }


    /**
     * @return the end
     */
    public T getEnd()
    {
        return end;
    }


    /**
     * @param end the end to set
     */
    public void setEnd(final T end)
    {
        this.end = end;
    }


    @Override
    public String toString()
    {
        final StringBuilder stringBuilder = new StringBuilder(200);
        stringBuilder.append("Range(start=").append(getStart()).append(", end=").append(getEnd()).append(")");
        return stringBuilder.toString();
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        final Range other = (Range)obj;
        if(end == null)
        {
            if(other.end != null)
            {
                return false;
            }
        }
        else if(!end.equals(other.end))
        {
            return false;
        }
        if(start == null)
        {
            if(other.start != null)
            {
                return false;
            }
        }
        else if(!start.equals(other.start))
        {
            return false;
        }
        return true;
    }
}
