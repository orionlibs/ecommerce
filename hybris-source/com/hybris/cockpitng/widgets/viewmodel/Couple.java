/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.viewmodel;

import java.io.Serializable;

/**
 * A simple couple class
 */
public class Couple<F, S> implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 904033664407207593L;
    private F first;
    private S second;


    public Couple()
    {
        this(null, null);
    }


    public Couple(final F first, final S second)
    {
        this.first = first;
        this.second = second;
    }


    /**
     * @return the first
     */
    public F getFirst()
    {
        return first;
    }


    /**
     * @param first the first to set
     */
    public void setFirst(final F first)
    {
        this.first = first;
    }


    /**
     * @return the second
     */
    public S getSecond()
    {
        return second;
    }


    /**
     * @param second the second to set
     */
    public void setSecond(final S second)
    {
        this.second = second;
    }
}
