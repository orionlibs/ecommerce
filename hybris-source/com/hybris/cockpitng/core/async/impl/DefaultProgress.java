/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.async.impl;

import com.hybris.cockpitng.core.async.Progress;

public class DefaultProgress implements Progress
{
    private int value;
    private boolean cancelRequested;


    @Override
    public void set(final int value)
    {
        this.value = value;
    }


    @Override
    public int get()
    {
        return value;
    }


    @Override
    public String toString()
    {
        return "DefaultProgress[" + value + "]_" + super.toString();
    }


    @Override
    public void requestCancel()
    {
        cancelRequested = true;
    }


    @Override
    public boolean isCancelRequested()
    {
        return cancelRequested;
    }
}
