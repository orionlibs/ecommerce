/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.lazyloading;

public class DefaultLazyTaskResult<LAZY_DATA> implements LazyTaskResult<LAZY_DATA>
{
    private final LAZY_DATA result;
    private final boolean isSuccess;


    public static <LAZY_DATA> DefaultLazyTaskResult<LAZY_DATA> success(final LAZY_DATA result)
    {
        return new DefaultLazyTaskResult<LAZY_DATA>(result, true);
    }


    public static <LAZY_DATA> DefaultLazyTaskResult<LAZY_DATA> failure()
    {
        return new DefaultLazyTaskResult<LAZY_DATA>(null, false);
    }


    private DefaultLazyTaskResult(final LAZY_DATA result, final boolean success)
    {
        this.result = result;
        isSuccess = success;
    }


    @Override
    public LAZY_DATA get()
    {
        return result;
    }


    @Override
    public boolean isSuccess()
    {
        return isSuccess;
    }
}
